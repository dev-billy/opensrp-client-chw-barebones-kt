package org.smartregister.chw.barebone.util

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import org.apache.commons.lang3.StringUtils
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.smartregister.chw.anc.domain.MemberObject
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.TestLibrary
import org.smartregister.chw.barebone.contract.BaseTestClientCallDialogContract
import org.smartregister.chw.barebone.contract.BaseTestClientCallDialogContract.Dialer
import org.smartregister.chw.barebone.custom_views.ClipboardDialog
import org.smartregister.clientandeventmodel.Event
import org.smartregister.repository.BaseRepository
import org.smartregister.util.PermissionUtils
import org.smartregister.util.Utils
import timber.log.Timber
import java.util.*

/**
 * This provides common utilities functions
 */
object TestUtil : KoinComponent {

    /**
     * Uses the [testLibrary] client processor to to save the given OpenSRP [baseEvent]
     */
    @JvmStatic
    @Throws(Exception::class)
    fun processEvent(testLibrary: TestLibrary, baseEvent: Event?) {
        if (baseEvent != null) {
            val eventJson =
                JSONObject(org.smartregister.util.JsonFormUtils.gson.toJson(baseEvent))
            testLibrary.syncHelper.addEvent(baseEvent.baseEntityId, eventJson)
            val lastSyncDate =
                Date(testLibrary.context.allSharedPreferences().fetchLastUpdatedAtDate(0))
            val eventClient =
                testLibrary.syncHelper.getEvents(lastSyncDate, BaseRepository.TYPE_Unsynced)
            Timber.i("EventClient = %s", Gson().toJson(eventClient))
            testLibrary.clientProcessorForJava.processClient(eventClient)
            Utils.getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.time)
        }
    }

    @JvmStatic
    fun fromHtml(text: String?): Spanned {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            }
            else -> {
                Html.fromHtml(text)
            }
        }
    }

    @JvmStatic
    fun getMemberProfileImageResourceIDentifier(): Int {
        return R.mipmap.ic_member
    }

    /**
     * Launches call dialer with [phoneNumber] using [activity] as the context from the [callView]
     */
    fun launchDialer(
        activity: Activity, callView: BaseTestClientCallDialogContract.View?, phoneNumber: String?
    ): Boolean = when {
        ContextCompat.checkSelfPermission(
            activity as Context, Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED
        -> { // set a pending call execution request
            if (callView != null) {
                callView.pendingCallRequest =
                    object : Dialer {
                        override fun callMe() {
                            launchDialer(activity, callView, phoneNumber)
                        }
                    }
            }
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.READ_PHONE_STATE),
                PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE
            )
            false
        }
        else -> {
            if ((activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number
                == null
            ) {
                Timber.i("No dial application so we launch copy to clipboard...")
                val clipboard =
                    activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(
                    activity.getText(R.string.copied_phone_number), phoneNumber
                )
                clipboard.primaryClip = clip
                ClipboardDialog(activity, R.style.ClipboardDialogStyle).also {
                    it.content = phoneNumber
                    it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    it.show()
                }
                // no phone
                Toast.makeText(
                    activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent =
                    Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
                activity.startActivity(intent)
            }
            true
        }
    }

    /**
     * Used to concantinate names and return the clients full names
     */
    @JvmStatic
    fun getFullName(memberObject: org.smartregister.chw.barebone.domain.MemberObject): String? {
        val nameBuilder = StringBuilder()
        val firstName = memberObject.firstName
        val lastName = memberObject.lastName
        val middleName = memberObject.middleName
        when {
            StringUtils.isNotBlank(firstName) -> {
                nameBuilder.append(firstName)
            }
            StringUtils.isNotBlank(middleName) -> {
                nameBuilder.append(" ")
                nameBuilder.append(middleName)
            }
            StringUtils.isNotBlank(lastName) -> {
                nameBuilder.append(" ")
                nameBuilder.append(lastName)
            }
        }
        return nameBuilder.toString()
    }


    @JvmStatic
    fun toMember(memberObject: org.smartregister.chw.barebone.domain.MemberObject): MemberObject {
        val res = MemberObject()
        res.baseEntityId = memberObject.baseEntityId
        res.firstName = memberObject.firstName
        res.lastName = memberObject.lastName
        res.middleName = memberObject.middleName
        res.dob = memberObject.age
        return res
    }


}