package org.smartregister.chw.barebone.contract

import android.content.Context

interface BaseTestCallDialogContract {
    interface View {
        var pendingCallRequest: Dialer?
        val currentContext: Context?
    }

    interface Dialer {
        fun callMe()
    }
}