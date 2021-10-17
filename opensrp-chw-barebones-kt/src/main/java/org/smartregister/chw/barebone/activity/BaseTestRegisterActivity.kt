package org.smartregister.chw.barebone.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MenuRes
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import org.json.JSONObject
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.contract.BaseTestRegisterContract
import org.smartregister.chw.barebone.fragment.BaseTestRegisterFragment
import org.smartregister.chw.barebone.listener.TestBottomNavigationListener
import org.smartregister.chw.barebone.model.BaseTestRegisterModel
import org.smartregister.chw.barebone.presenter.BaseTestRegisterPresenter
import org.smartregister.chw.barebone.util.Constants
import org.smartregister.helper.BottomNavigationHelper
import org.smartregister.listener.BottomNavigationListener
import org.smartregister.view.activity.BaseRegisterActivity

/**
 * Created by cozej4 on 2020-05-13.
 *
 * @cozej4 https://github.com/cozej4
 */

/***
 * This class is for displaying register for all the tb clients
 * it implements [BaseTestRegisterContract.View]
 */
open class BaseTestRegisterActivity : BaseRegisterActivity(),
    BaseTestRegisterContract.View {

    protected var baseEntityId: String? = null
    protected var formName: String? = null
    protected var formAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(this.intent) {
            baseEntityId = getStringExtra(Constants.ActivityPayload.BASE_ENTITY_ID)
            formAction = getStringExtra(Constants.ActivityPayload.ACTION)
            formName = getStringExtra(Constants.ActivityPayload.TB_REGISTRATION_FORM_NAME)
            onStartActivityWithAction()
        }
    }

    /**
     * Process a payload when an activity is started with an action
     */
    protected open fun onStartActivityWithAction() {
        if (formName != null && formAction != null) {
            startFormActivity(formName, baseEntityId, formAction)
        }
    }
    override fun startRegistration() = Unit

    override fun startFormActivity(formName: String?, entityId: String?, metaData: String?) = Unit

    override fun startFormActivity(jsonForm: JSONObject) {
        val intent = Intent(this, familyFormActivity)
            .putExtra(Constants.JsonFormExtra.JSON, jsonForm.toString())
        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON)
    }

    val familyFormActivity get() = BaseTestRegisterActivity::class.java

    override fun onActivityResultExtended(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    override fun getViewIdentifiers(): List<String> = listOf(Constants.Configuration.TB_REGISTER)

    /**
     * Override this to subscribe to bottom navigation
     */
    override fun registerBottomNavigation() {
        bottomNavigationHelper = BottomNavigationHelper()
        bottomNavigationView =
            findViewById(org.smartregister.R.id.bottom_navigation)
        bottomNavigationView?.also {
            it.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            it.menu.removeItem(org.smartregister.R.id.action_clients)
            it.menu.removeItem(R.id.action_register)
            it.menu.removeItem(org.smartregister.R.id.action_search)
            it.menu.removeItem(org.smartregister.R.id.action_library)
            it.inflateMenu(menuResource)
            bottomNavigationHelper.disableShiftMode(it)
            it.setOnNavigationItemSelectedListener(getBottomNavigation(this))
        }
    }

    @get:MenuRes
    val menuResource
        get() = R.menu.bottom_nav_test_menu

    protected open fun getBottomNavigation(activity: Activity?): BottomNavigationListener =
        TestBottomNavigationListener(activity!!)

    override fun initializePresenter() {
        presenter = BaseTestRegisterPresenter(this, BaseTestRegisterModel())
    }

    override fun getRegisterFragment() = BaseTestRegisterFragment()
    override fun getOtherFragments() = arrayOf(BaseTestRegisterFragment())

    // override fun getOtherFragments() = arrayOf(BaseTbCommunityFollowupRegisterFragment())

    override fun presenter() = presenter as BaseTestRegisterContract.Presenter
}