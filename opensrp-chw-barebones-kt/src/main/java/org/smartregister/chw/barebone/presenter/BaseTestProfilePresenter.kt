package org.smartregister.chw.barebone.presenter

import org.smartregister.chw.barebone.contract.BaseTestProfileContract
import org.smartregister.chw.barebone.contract.BaseTestProfileContract.InteractorCallback
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import java.util.*

open class BaseTestProfilePresenter(
    override val view: BaseTestProfileContract.View?,
    val interactor: BaseTestProfileContract.Interactor,
    var memberObject: MemberObject
) : BaseProfileContract, BaseTestProfileContract.Presenter,
    InteractorCallback {
    override fun refreshProfileData() {
        view?.showFollowUpVisitButton(true)
        interactor.refreshProfileView(memberObject, false, this)
    }

    override fun refreshProfileTbStatusInfo() {
        interactor.updateProfileTbStatusInfo(memberObject, this)
    }

    override fun refreshLastVisit(lastVisitDate: Date?) {
        view?.updateLastVisitRow(lastVisitDate)
    }

    override fun refreshProfileTopSection(memberObject: MemberObject?) {
        view?.setProfileViewDetails(memberObject)
        view?.showProgressBar(false)
    }

    override fun refreshUpComingServicesStatus(
        service: String?,
        status: AlertStatus?,
        date: Date?
    ) {
        view?.setUpComingServicesStatus(service, status, date)
    }

    override fun refreshFamilyStatus(status: AlertStatus?) {
        view?.setFamilyStatus(status)
    }
}