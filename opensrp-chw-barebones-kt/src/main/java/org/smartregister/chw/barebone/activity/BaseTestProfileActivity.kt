package org.smartregister.chw.barebone.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import de.hdodenhof.circleimageview.CircleImageView
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Period
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.contract.BaseTestProfileContract
import org.smartregister.chw.barebone.custom_views.BaseTestFloatingMenu
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.chw.barebone.interactor.BaseTestProfileInteractor
import org.smartregister.chw.barebone.presenter.BaseTestProfilePresenter
import org.smartregister.chw.barebone.util.Constants
import org.smartregister.chw.barebone.util.TestUtil.fromHtml
import org.smartregister.chw.barebone.util.TestUtil.getMemberProfileImageResourceIDentifier
import org.smartregister.domain.AlertStatus
import org.smartregister.helper.ImageRenderHelper
import org.smartregister.view.activity.BaseProfileActivity
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

open class BaseTestProfileActivity : BaseProfileActivity(),
    BaseTestProfileContract.View {
    private var lastVisitRow: View? = null
    private var recordFollowUpVisitLayout: LinearLayout? = null
    private var recordVisitStatusBarLayout: RelativeLayout? = null
    private var tickImage: ImageView? = null
    private var tvEditVisit: TextView? = null
    private var tvUndo: TextView? = null
    private var tvVisitDone: TextView? = null
    private var rlLastVisitLayout: RelativeLayout? = null
    private var rlUpcomingServices: RelativeLayout? = null
    private var rlFamilyServicesDue: RelativeLayout? = null
    private var tvLastVisitDay: TextView? = null
    private var tvViewMedicalHistory: TextView? = null
    private var tvUpComingServices: TextView? = null
    private var tvFamilyStatus: TextView? = null
    private var tvFamilyProfile: TextView? = null
    private var tvRecordTbFollowUp: TextView? = null
    private var tvTbRow: TextView? = null
    var testProfilePresenter: BaseTestProfileContract.Presenter? = null
    var testFloatingMenu: BaseTestFloatingMenu? = null
    var memberObject: MemberObject? = null
    private var numOfDays = 0
    private var progressBar: ProgressBar? = null
    private var profileImageView: CircleImageView? = null
    private var tvName: TextView? = null
    private var tvGender: TextView? = null
    private var tvLocation: TextView? = null
    private var tvUniqueID: TextView? = null
    private var overDueRow: View? = null
    private var familyRow: View? = null
    override fun onCreation() {
        setContentView(R.layout.activity_base_test_profile)
        val toolbar =
            findViewById<Toolbar>(R.id.collapsing_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            val upArrow =
                resources.getDrawable(R.drawable.ic_arrow_back_white_24dp)
            upArrow.setColorFilter(
                resources.getColor(R.color.text_blue),
                PorterDuff.Mode.SRC_ATOP
            )
            actionBar.setHomeAsUpIndicator(upArrow)
        }
        toolbar.setNavigationOnClickListener { v: View? -> finish() }
        appBarLayout = findViewById(R.id.collapsing_toolbar_appbarlayout)
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.outlineProvider = null
        }
        memberObject =
            intent.getSerializableExtra(Constants.ActivityPayload.MEMBER_OBJECT) as MemberObject
        setupViews()
        initializePresenter()
        fetchProfileData()
        initializeCallFAB()
    }

    override fun setupViews() {
        imageRenderHelper = ImageRenderHelper(this)
        tvName = findViewById(R.id.textview_name)
        tvGender = findViewById(R.id.textview_gender)
        tvLocation = findViewById(R.id.textview_address)
        tvUniqueID = findViewById(R.id.textview_unique_id)
        lastVisitRow = findViewById(R.id.view_last_visit_row)
        overDueRow = findViewById(R.id.view_most_due_overdue_row)
        familyRow = findViewById(R.id.view_family_row)
        tvUpComingServices = findViewById(R.id.textview_name_due)
        tvFamilyStatus = findViewById(R.id.textview_family_has)
        tvFamilyProfile = findViewById(R.id.text_view_family_profile)
        tvTbRow = findViewById(R.id.textview_test_registration_date_row)
        rlLastVisitLayout = findViewById(R.id.rl_last_visit_layout)
        tvLastVisitDay = findViewById(R.id.textview_last_vist_day)
        tvViewMedicalHistory = findViewById(R.id.textview_medical_history)
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices)
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue)
        progressBar = findViewById(R.id.progress_bar)
        tickImage = findViewById(R.id.tick_image)
       // tvVisitDone = findViewById(R.id.textview_visit_done)
        tvEditVisit = findViewById(R.id.textview_edit)
        tvUndo = findViewById(R.id.textview_undo)
        profileImageView =
            findViewById(R.id.imageview_profile)
        tvRecordTbFollowUp = findViewById(R.id.textview_record_reccuring_visit)
        tvUndo?.let { tvUndo?.setOnClickListener(this) }
        tvEditVisit?.let { tvEditVisit?.setOnClickListener(this) }
        tvRecordTbFollowUp?.let { tvRecordTbFollowUp?.setOnClickListener(this) }
        findViewById<View>(R.id.rl_last_visit_layout).setOnClickListener(this)
        findViewById<View>(R.id.rlUpcomingServices).setOnClickListener(this)
        findViewById<View>(R.id.rlFamilyServicesDue).setOnClickListener(this)
        findViewById<View>(R.id.rltestRegistrationDate).setOnClickListener(this)
    }

    override fun initializePresenter() {
        testProfilePresenter =
            BaseTestProfilePresenter(this, BaseTestProfileInteractor(), memberObject!!)
    }

    open fun initializeCallFAB() {
        if (StringUtils.isNotBlank(memberObject!!.phoneNumber)
            || StringUtils.isNotBlank(memberObject!!.familyHeadPhoneNumber)
        ) {
            testFloatingMenu = BaseTestFloatingMenu(this, memberObject!!)
            testFloatingMenu!!.gravity = Gravity.BOTTOM or Gravity.END
            val linearLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            addContentView(testFloatingMenu, linearLayoutParams)
        }
    }

    override fun setupViewPager(viewPager: ViewPager): ViewPager? {
        return null
    }

    override fun fetchProfileData() {
        testProfilePresenter!!.refreshProfileData()
        testProfilePresenter!!.refreshProfileTbStatusInfo()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.title_layout) {
            onBackPressed()
        } else if (id == R.id.rl_last_visit_layout) {
            openMedicalHistory()
        } else if (id == R.id.rlUpcomingServices) {
            openUpcomingServices()
        } else if (id == R.id.rlFamilyServicesDue) {
            openFamilyDueServices()
        } else if (id == R.id.textview_record_reccuring_visit) {
            openFollowUpVisitForm(false)
        } else if (id == R.id.textview_edit) {
            openFollowUpVisitForm(true)
        }
    }

    override fun setupFollowupVisitEditViews(isWithin24Hours: Boolean) {
        if (isWithin24Hours) {
            recordFollowUpVisitLayout!!.visibility = View.GONE
            recordVisitStatusBarLayout!!.visibility = View.GONE
            tvEditVisit!!.visibility = View.VISIBLE;
        } else {
            tvEditVisit!!.visibility = View.GONE
            recordFollowUpVisitLayout!!.visibility = View.VISIBLE
            recordVisitStatusBarLayout!!.visibility = View.GONE
        }
    }

    override val context: Context
        get() = this

    override fun openMedicalHistory() {
        // TODO :: Open medical history view
    }

    override fun openUpcomingServices() {
        // TODO :: Show upcoming services
    }

    override fun openFamilyDueServices() {
        // TODO :: Show family due services
    }

    override fun openFollowUpVisitForm(isEdit: Boolean) {
        // TODO :: Open follow-up visit form for editing
    }

    override fun setUpComingServicesStatus(
        service: String?,
        status: AlertStatus?,
        date: Date?
    ) {
        showProgressBar(false)
        val dateFormat =
            SimpleDateFormat("dd MMM", Locale.getDefault())
        if (status == AlertStatus.complete) return
        overDueRow!!.visibility = View.VISIBLE
        rlUpcomingServices!!.visibility = View.VISIBLE
        if (status == AlertStatus.upcoming) {
            tvUpComingServices!!.text = fromHtml(
                getString(
                    R.string.test_upcoming_visit,
                    service,
                    dateFormat.format(date)
                )
            )
        } else {
            tvUpComingServices!!.text = fromHtml(
                getString(
                    R.string.test_service_due,
                    service,
                    dateFormat.format(date)
                )
            )
        }
    }

    override fun setFamilyStatus(status: AlertStatus?) {
        findViewById<View>(R.id.rltestRegistrationDate).visibility = View.VISIBLE
        familyRow!!.visibility = View.VISIBLE
        rlFamilyServicesDue!!.visibility = View.VISIBLE

        when {
            memberObject?.familyMemberEntityType.equals(Constants.FamilyMemberEntityType.EC_INDEPENDENT_CLIENT) -> {
                when (status) {
                    AlertStatus.complete -> {
                        tvFamilyStatus!!.text = getString(R.string.client_has_nothing_due)
                    }
                    AlertStatus.normal -> {
                        tvFamilyStatus!!.text = getString(R.string.client_has_services_due)
                    }
                    AlertStatus.urgent -> {
                        tvFamilyStatus!!.text =
                            fromHtml(getString(R.string.client_has_service_overdue))
                    }
                    else -> {
                        tvFamilyStatus!!.text = getString(R.string.client_has_nothing_due)
                    }
                }
                tvFamilyProfile!!.text = getString(R.string.go_to_client_s_profile)
            }
            else -> {
                when (status) {
                    AlertStatus.complete -> {
                        tvFamilyStatus!!.text = getString(R.string.family_has_nothing_due)
                    }
                    AlertStatus.normal -> {
                        tvFamilyStatus!!.text = getString(R.string.family_has_services_due)
                    }
                    AlertStatus.urgent -> {
                        tvFamilyStatus!!.text =
                            fromHtml(getString(R.string.family_has_service_overdue))
                    }
                    else -> {
                        tvFamilyStatus!!.text = getString(R.string.family_has_nothing_due)
                    }
                }
                tvFamilyProfile!!.text = getString(R.string.go_to_family_s_profile)

            }
        }
    }

    override fun setProfileViewDetails(memberObject: MemberObject?) {
        val age = Period(
            DateTime(memberObject!!.age),
            DateTime()
        ).years
        tvName!!.text = String.format(
            Locale.getDefault(), "%s %s %s, %d", memberObject.firstName,
            memberObject.middleName, memberObject.lastName, age
        )
        tvGender!!.text = memberObject.gender
        tvLocation!!.text = memberObject.address
        tvUniqueID!!.text = memberObject.uniqueId
        imageRenderHelper.refreshProfileImage(
            memberObject.baseEntityId,
            profileImageView,
            getMemberProfileImageResourceIDentifier()
        )
        tvTbRow!!.text = String.format(
            getString(R.string.test_client_registered_text),
            getString(R.string.test_on),
            memberObject.tbRegistrationDate
        )
        if (StringUtils.isNotBlank(memberObject.familyHead) && memberObject.familyHead == memberObject.baseEntityId) {
            findViewById<View>(R.id.tb_family_head).visibility = View.VISIBLE
        }
        if (StringUtils.isNotBlank(memberObject.primaryCareGiver) && memberObject.primaryCareGiver == memberObject.baseEntityId) {
            findViewById<View>(R.id.tb_primary_caregiver).visibility = View.VISIBLE
        }
    }

    private fun formatTime(dateTime: String): CharSequence? {
        var timePassedString: CharSequence? = null
        try {
            val df =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = df.parse(dateTime)
            timePassedString =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(date)
        } catch (e: Exception) {
            Timber.d(e)
        }
        return timePassedString
    }

    override fun updateLastVisitRow(lastVisitDate: Date?) {
        showProgressBar(false)
        if (lastVisitDate == null) return
        tvLastVisitDay!!.visibility = View.VISIBLE
        numOfDays = Days.daysBetween(
            DateTime(lastVisitDate).toLocalDate(),
            DateTime().toLocalDate()
        ).days
        tvLastVisitDay!!.text = getString(
            R.string.last_visit_n_days_ago,
            if (numOfDays <= 1) getString(R.string.less_than_twenty_four) else "$numOfDays " + getString(
                R.string.days
            )
        )
        rlLastVisitLayout!!.visibility = View.GONE
        lastVisitRow!!.visibility = View.GONE
    }

    override fun onMemberDetailsReloaded(memberObject: MemberObject?) {
        setupViews()
        fetchProfileData()
    }

    override fun setFollowUpButtonDue() {
        showFollowUpVisitButton(true)
        tvRecordTbFollowUp!!.background = resources.getDrawable(R.drawable.record_tb_followup)
    }

    override fun setFollowUpButtonOverdue() {
        showFollowUpVisitButton(true)
        tvRecordTbFollowUp!!.background =
            resources.getDrawable(R.drawable.record_tb_followup_overdue)
    }

    override fun showFollowUpVisitButton(status: Boolean) {
        if (status) tvRecordTbFollowUp!!.visibility =
            View.VISIBLE else tvRecordTbFollowUp!!.visibility = View.GONE
    }

    override fun hideFollowUpVisitButton() {
        tvRecordTbFollowUp!!.visibility = View.GONE
    }

    override fun showProgressBar(status: Boolean) {
        progressBar!!.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun openTbRegistrationForm() {}

    companion object {
        fun startProfileActivity(activity: Activity, memberObject: MemberObject) {
            val intent = Intent(activity, BaseTestProfileActivity::class.java)
            intent.putExtra(Constants.ActivityPayload.MEMBER_OBJECT, memberObject)
            activity.startActivity(intent)
        }
    }
}