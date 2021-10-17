package org.smartregister.chw.barebone.presenter

import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.barebone.TestApp
import org.smartregister.chw.barebone.contract.BaseTestRegisterFragmentContract
import org.smartregister.chw.barebone.model.BaseTestRegisterFragmentModel
import org.smartregister.chw.barebone.util.Constants
import org.smartregister.chw.barebone.util.DBConstants

/**
 * Test class for testing various methods in BaseTestRegisterFragmentPresenter
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class)
class BaseTestRegisterFragmentPresenterTest {

    private val testRegisterFragmentView: BaseTestRegisterFragmentContract.View = spyk()

    private val tbRegisterFragmentPresenter: BaseTestRegisterFragmentContract.Presenter =
        spyk(
            BaseTestRegisterFragmentPresenter(
                testRegisterFragmentView, BaseTestRegisterFragmentModel(), null
            ),
            recordPrivateCalls = true
        )


    @Test
    fun `Should initialize the queries on the view`() {
        tbRegisterFragmentPresenter.initializeQueries("ec_tb_register.client_tb_status_during_registration")
        val visibleColumns =
            (tbRegisterFragmentPresenter as BaseTestRegisterFragmentPresenter).visibleColumns
        verifySequence {
            testRegisterFragmentView.initializeQueryParams(
                "ec_tb_register",
                "SELECT COUNT(*) FROM ec_tb_register WHERE ec_tb_register.client_tb_status_during_registration ",
                "Select ec_tb_register.id as _id , ec_tb_register.relationalid FROM ec_tb_register WHERE ec_tb_register.client_tb_status_during_registration "
            )
            testRegisterFragmentView.initializeAdapter(visibleColumns)
            testRegisterFragmentView.countExecute()
            testRegisterFragmentView.filterandSortInInitializeQueries()
        }
    }

    @Test
    fun `Main condition should be initialize by empty string`() {
        assertTrue(tbRegisterFragmentPresenter.getMainCondition().isEmpty())
    }

    @Test
    fun `Should return the right table name`() {
        assertTrue(tbRegisterFragmentPresenter.getMainTable() == Constants.Tables.TB)
    }

    @Test
    fun `Should return the due filter query`() {
        assertEquals(
            tbRegisterFragmentPresenter.getDueFilterCondition(),
            "ec_tb_register.client_tb_status_during_registration = '${Constants.TbStatus.UNKNOWN}'"
        )
    }

    @Test
    fun `Should return default sort query`() {
        assertEquals(
            tbRegisterFragmentPresenter.getDefaultSortQuery(),
            Constants.Tables.TB + "." + DBConstants.Key.TB_REGISTRATION_DATE + " DESC "
        )
    }

    @Test
    fun `View should not be null`() {
        assertNotNull(tbRegisterFragmentPresenter.getView())
    }
}