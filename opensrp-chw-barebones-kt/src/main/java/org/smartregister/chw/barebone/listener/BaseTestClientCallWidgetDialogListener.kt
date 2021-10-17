package org.smartregister.chw.barebone.listener

import android.view.View
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.fragment.BaseTestCallDialogFragment
import org.smartregister.chw.barebone.util.TestUtil
import timber.log.Timber

/**
 * This is the listener implementation for the provided [callDialogFragment]. It handles the click listeners
 */
class BaseTestClientCallWidgetDialogListener(private val callDialogFragment: BaseTestCallDialogFragment) :
    View.OnClickListener {
    override fun onClick(view: View) {
        when (view.id) {
            R.id.tb_call_close -> {
                callDialogFragment.dismiss()
            }
            R.id.tb_call_head_phone, R.id.call_tb_client_phone -> {
                try {
                    val phoneNumber = view.tag as String
                    TestUtil.launchDialer(callDialogFragment.activity, callDialogFragment, phoneNumber)
                    callDialogFragment.dismiss()
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }
            }
        }
    }

}