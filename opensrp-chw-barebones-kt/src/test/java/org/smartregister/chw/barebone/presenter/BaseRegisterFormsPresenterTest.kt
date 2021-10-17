package org.smartregister.chw.barebone.presenter

import io.mockk.spyk
import io.mockk.verifyAll
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.barebone.TestApp
import org.smartregister.chw.barebone.contract.BaseRegisterFormsContract
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.chw.barebone.interactor.BaseRegisterFormsInteractor
import org.smartregister.chw.barebone.model.BaseTestRegisterFragmentModel
import org.smartregister.chw.barebone.util.Constants
import org.smartregister.chw.barebone.util.DBConstants
import org.smartregister.commonregistry.CommonPersonObjectClient

/**
 * Test class for testing various methods in BaseRegisterFormsPresenter
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class)
class BaseRegisterFormsPresenterTest {

    private val registerFormsView: BaseRegisterFormsContract.View = spyk()
    private val registerFormsInteractor = BaseRegisterFormsInteractor()
    private val sampleBaseEntityId = "5a5mple-b35eent"
    private val baseRegisterFormsPresenter: BaseRegisterFormsContract.Presenter =
        spyk(
            BaseRegisterFormsPresenter(
                sampleBaseEntityId, registerFormsView,
                registerFormsInteractor
            ),
            recordPrivateCalls = true
        )
    private lateinit var memberObject: MemberObject

    @Before
    fun `Just before tests`() {
        val columnNames = BaseTestRegisterFragmentModel()
            .mainColumns(Constants.Tables.FAMILY_MEMBER).map {
                it.replace("${Constants.Tables.FAMILY_MEMBER}.", "")
            }.toTypedArray()

        val columnValues = arrayListOf(
            "10689167-07ca-45a4-91c0-1406c9ceb881", sampleBaseEntityId, "first_name", "middle_name",
            "last_name", "unique_id", "male", "1985-01-01T03:00:00.000+03:00", null
        )
        val details = columnNames.zip(columnValues).toMap()
        memberObject = MemberObject(
            CommonPersonObjectClient(sampleBaseEntityId, details, "Some Cool Name").apply {
                columnmaps = details
            }
        )
    }

    @Test
    fun `Should return view`() {
        Assert.assertNotNull(baseRegisterFormsPresenter.getView())
    }

    @Test
    fun `Should return main condition with the base entity id`() {
        Assert.assertTrue(
            baseRegisterFormsPresenter.getMainCondition() == "${Constants.Tables.FAMILY_MEMBER}.${DBConstants.Key.BASE_ENTITY_ID}  = '$sampleBaseEntityId'"
        )
    }

    @Test
    fun `Should return ec_family_member as the main table`() {
        Assert.assertTrue(baseRegisterFormsPresenter.getMainTable() == Constants.Tables.FAMILY_MEMBER)
    }

    @Test
    fun `Should set profile with data  `() {
        baseRegisterFormsPresenter.fillClientData(memberObject)
        verifyAll { registerFormsView.setProfileViewWithData() }
    }

    @Test
    fun `Should update the member object`() {
        baseRegisterFormsPresenter.initializeMemberObject(memberObject)
        Assert.assertNotNull((baseRegisterFormsPresenter as BaseRegisterFormsPresenter).memberObject)
    }
}