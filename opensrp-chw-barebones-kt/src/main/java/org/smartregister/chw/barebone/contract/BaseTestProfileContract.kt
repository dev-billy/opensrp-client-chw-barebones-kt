package org.smartregister.chw.barebone.contract

import android.content.Context
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import java.util.*

interface BaseTestProfileContract {
    interface View : BaseProfileContract.View {
        val context: Context?
        fun openMedicalHistory()
        fun openUpcomingServices()
        fun openFamilyDueServices()
        fun openTbRegistrationForm()
        fun openFollowUpVisitForm(isEdit: Boolean)
        fun setUpComingServicesStatus(
            service: String?,
            status: AlertStatus?,
            date: Date?
        )

        fun setFamilyStatus(status: AlertStatus?)
        fun setProfileViewDetails(memberObject: MemberObject?)
        fun setupFollowupVisitEditViews(isWithin24Hours: Boolean)
        fun updateLastVisitRow(lastVisitDate: Date?)
        fun setFollowUpButtonOverdue()
        fun setFollowUpButtonDue()
        fun hideFollowUpVisitButton()
        fun showFollowUpVisitButton(status: Boolean)
        fun showProgressBar(status: Boolean)
        fun onMemberDetailsReloaded(memberObject: MemberObject?)
    }

    interface Presenter {
        val view: View?
        fun refreshProfileData()
        fun refreshProfileTbStatusInfo()
    }

    interface Interactor {
        fun refreshProfileView(
            memberObject: MemberObject?,
            isForEdit: Boolean,
            callback: InteractorCallback?
        )

        fun updateProfileTbStatusInfo(
            memberObject: MemberObject?,
            callback: InteractorCallback?
        )
    }

    interface InteractorCallback {
        fun refreshProfileTopSection(memberObject: MemberObject?)
        fun refreshUpComingServicesStatus(
            service: String?,
            status: AlertStatus?,
            date: Date?
        )

        fun refreshFamilyStatus(status: AlertStatus?)
        fun refreshLastVisit(lastVisitDate: Date?)
    }
}