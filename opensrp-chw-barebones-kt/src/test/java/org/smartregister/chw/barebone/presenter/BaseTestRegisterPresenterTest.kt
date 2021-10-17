package org.smartregister.chw.barebone.presenter

import io.mockk.spyk
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.smartregister.chw.barebone.TestApp
import org.smartregister.chw.barebone.contract.BaseTestRegisterContract
import org.smartregister.chw.barebone.model.BaseTestRegisterModel

/**
 * Test class for testing various methods in BaseTestRegisterPresenter
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class)
class BaseTestRegisterPresenterTest {

    private val testHistoryView: BaseTestRegisterContract.View = spyk()
    private val testHistoryPresenter: BaseTestRegisterContract.Presenter =
        spyk(
            BaseTestRegisterPresenter(testHistoryView, BaseTestRegisterModel()), recordPrivateCalls = true
        )

    @Test
    fun `Referral register view should not be null`() {
        assertNotNull(testHistoryPresenter.getView())
    }
}