package org.smartregister.chw.barebone.presenter

import android.app.Activity
import android.database.sqlite.SQLiteException
import android.util.Log
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.tuple.Triple
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.contract.BaseRegisterFormsContract
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.chw.barebone.util.Constants
import org.smartregister.chw.barebone.util.DBConstants
import org.smartregister.util.Utils
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

open class BaseRegisterFormsPresenter(
    val baseEntityID: String,
    view: BaseRegisterFormsContract.View,
    protected var interactor: BaseRegisterFormsContract.Interactor
) : BaseRegisterFormsContract.Presenter, BaseRegisterFormsContract.InteractorCallBack {

    var memberObject: MemberObject? = null
    private var viewReference = WeakReference(view)

    override fun getView(): BaseRegisterFormsContract.View? {
        return viewReference.get()
    }

    override fun getMainCondition() =
        "${Constants.Tables.FAMILY_MEMBER}.${DBConstants.Key.BASE_ENTITY_ID}  = '$baseEntityID'"

    override fun getMainTable() = Constants.Tables.FAMILY_MEMBER

    override fun fillClientData(memberObject: MemberObject?) {
        if (getView() != null) {
            getView()?.setProfileViewWithData()
        }
    }

    override fun initializeMemberObject(memberObject: MemberObject?) {
        this.memberObject = memberObject
    }

    override fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject) {
        try {
            interactor.saveRegistration(baseEntityID, valuesHashMap, jsonObject, this)
        } catch (e: JSONException) {
            Timber.e(Log.getStackTraceString(e))
        } catch (e: SQLiteException) {
            Timber.e(Log.getStackTraceString(e))
        }
    }

    override fun onUniqueIdFetched(triple: Triple<String, String, String>, entityId: String) = Unit

    override fun onNoUniqueId() = Unit

    override fun onRegistrationSaved(saveSuccessful: Boolean, encounterType: String) {
        val context = getView() as Activity
        val toastMessage = when {
            saveSuccessful && encounterType == Constants.EventType.REGISTRATION -> context.getString(
                R.string.successful_test_registration
            )
            saveSuccessful && encounterType == Constants.EventType.TB_CASE_CLOSURE -> context.getString(
                R.string.successful_test_case_closure
            )
            saveSuccessful && encounterType == Constants.EventType.FOLLOW_UP_VISIT -> context.getString(
                R.string.successful_visit
            )
            saveSuccessful && encounterType == Constants.EventType.TB_OUTCOME -> context.getString(R.string.successful_visit)
            saveSuccessful && encounterType == Constants.EventType.TB_COMMUNITY_FOLLOWUP -> context.getString(
                R.string.test_community_followup_referral_issued
            )
            saveSuccessful && encounterType == Constants.EventType.TB_COMMUNITY_FOLLOWUP_FEEDBACK -> context.getString(
                R.string.test_community_followup_feedback_saved
            )
            saveSuccessful -> context.getString(
                R.string.form_saved
            )
            else -> context.getString(R.string.form_not_saved)
        }
        Utils.showToast(context, toastMessage)
    }
}