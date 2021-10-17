package org.smartregister.chw.barebone.custom_views

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.chw.barebone.fragment.BaseTestCallDialogFragment
import org.smartregister.chw.barebone.util.TestUtil

open class BaseTestFloatingMenu (context: Context?, val memberObject: MemberObject): LinearLayout(context), View.OnClickListener {

    open fun initUi() {
        View.inflate(context, R.layout.test_call_floating_menu, this)
        findViewById<View>(R.id.tb_fab).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tb_fab) {
            val activity = context as Activity
            BaseTestCallDialogFragment.launchDialog(
                activity,
                TestUtil.getFullName(this.memberObject),
                memberObject.phoneNumber,
                memberObject.primaryCareGiver,
                memberObject.primaryCareGiverPhoneNumber
            )
        }
    }

    init {
        initUi()
    }

}