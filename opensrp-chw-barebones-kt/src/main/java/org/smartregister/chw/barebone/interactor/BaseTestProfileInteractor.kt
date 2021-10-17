package org.smartregister.chw.barebone.interactor

import androidx.annotation.VisibleForTesting
import org.smartregister.chw.anc.util.AppExecutors
import org.smartregister.chw.barebone.contract.BaseTestProfileContract
import org.smartregister.chw.barebone.contract.BaseTestProfileContract.InteractorCallback
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.domain.AlertStatus
import java.util.*

open class BaseTestProfileInteractor @VisibleForTesting internal constructor(
    var appExecutors: AppExecutors
) : BaseTestProfileContract.Interactor {

    constructor() : this(AppExecutors()) {}

    override fun refreshProfileView(
        memberObject: MemberObject?,
        isForEdit: Boolean,
        callback: InteractorCallback?
    ) {
        val runnable = Runnable {
            appExecutors.mainThread()
                .execute { callback!!.refreshProfileTopSection(memberObject) }
        }
        appExecutors.diskIO().execute(runnable)
    }

    override fun updateProfileTbStatusInfo(
        memberObject: MemberObject?,
        callback: InteractorCallback?
    ) {
        val runnable = Runnable {
            appExecutors.mainThread().execute {
                callback!!.refreshFamilyStatus(AlertStatus.normal)
                callback.refreshUpComingServicesStatus(
                    "TB Followup Visit",
                    AlertStatus.normal,
                    Date()
                )
                callback.refreshLastVisit(Date())
            }
        }
        appExecutors.diskIO().execute(runnable)
    }

}