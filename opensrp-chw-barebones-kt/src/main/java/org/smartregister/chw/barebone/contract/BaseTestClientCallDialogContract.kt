package org.smartregister.chw.barebone.contract

interface BaseTestClientCallDialogContract {

    interface View {

        var pendingCallRequest: Dialer?

    }

    interface Model {
        var name: String?
    }

    interface Dialer {
        fun callMe()
    }
}