package org.smartregister.chw.barebone.fragment

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.smartregister.chw.barebone.R
import org.smartregister.chw.barebone.contract.BaseTestRegisterFragmentContract
import org.smartregister.chw.barebone.dao.TestDao
import org.smartregister.chw.barebone.domain.MemberObject
import org.smartregister.chw.barebone.model.BaseTestRegisterFragmentModel
import org.smartregister.chw.barebone.presenter.BaseTestRegisterFragmentPresenter
import org.smartregister.chw.barebone.provider.BaseTestRegisterProvider
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.configurableviews.model.View
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter
import org.smartregister.cursoradapter.RecyclerViewProvider
import org.smartregister.view.customcontrols.CustomFontTextView
import org.smartregister.view.customcontrols.FontVariant
import org.smartregister.view.fragment.BaseRegisterFragment
import java.util.*

/**
 * This register displays list of all the referred clients, it implements [BaseTestRegisterFragmentContract.View] and extends
 * OpenSRP's [BaseRegisterFragment] which provides common functionality and consistency when creating
 * register.
 *
 */
open class BaseTestRegisterFragment : BaseRegisterFragment(),
    BaseTestRegisterFragmentContract.View {

    @Suppress("INACCESSIBLE_TYPE")
    override fun initializeAdapter(visibleColumns: Set<View>?) {
        val referralRegisterProvider = BaseTestRegisterProvider(
            activity as Context, paginationViewHandler, registerActionHandler, visibleColumns
        )
        clientAdapter = RecyclerViewPaginatedAdapter<BaseTestRegisterProvider.RegisterViewHolder>(
            null,
            referralRegisterProvider as RecyclerViewProvider<RecyclerView.ViewHolder>,
            context().commonrepository(tablename)
        )
        clientAdapter.setCurrentlimit(20)
        clientsView.adapter = clientAdapter
    }

    override fun setupViews(view: android.view.View) {
        super.setupViews(view)
        // Update top left icon
        view.findViewById<ImageView>(org.smartregister.R.id.scanQrCode)?.apply {
            visibility = android.view.View.GONE
        }
        // Update Search bar
        view.findViewById<android.view.View>(org.smartregister.R.id.search_bar_layout)
            ?.apply { setBackgroundResource(R.color.customAppThemeBlue) }

        getSearchView()?.apply {
            getSearchView().setBackgroundResource(R.color.white)
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_action_search, 0, 0, 0
            )
        }
        // Update sort filter
        view.findViewById<TextView>(org.smartregister.R.id.filter_text_view)
            ?.apply {
                text = getString(R.string.sort)
            }
        // Update title name
        view.findViewById<ImageView>(org.smartregister.R.id.opensrp_logo_image_view)
            ?.apply {
                visibility = android.view.View.GONE
            }
        view.findViewById<CustomFontTextView>(R.id.txt_title_label)?.apply {
            visibility = android.view.View.VISIBLE
            text = getString(R.string.test_clients)
            setFontVariant(FontVariant.REGULAR)
        }
    }

    override fun presenter() = presenter as BaseTestRegisterFragmentContract.Presenter

    override fun initializePresenter() {
        if (activity == null) {
            return
        }
        presenter =
            BaseTestRegisterFragmentPresenter(this, BaseTestRegisterFragmentModel(), null)
    }

    override fun setUniqueID(s: String) {
        if (getSearchView() != null) {
            getSearchView().setText(s)
        }
    }

    override fun setAdvancedSearchFormData(hashMap: HashMap<String, String>) = Unit

    override fun getMainCondition() = presenter().getMainCondition()

    override fun getDefaultSortQuery() = presenter().getDefaultSortQuery()

    override fun startRegistration() = Unit

    override fun onViewClicked(view: android.view.View) {
        if (activity == null) {
            return
        }
        if (view.tag is CommonPersonObjectClient && view.getTag(R.id.VIEW_ID) === CLICK_VIEW_NORMAL) {
            openProfile(view.tag as CommonPersonObjectClient)
        } else if (view.tag is CommonPersonObjectClient && view.getTag(R.id.VIEW_ID) === FOLLOW_UP_VISIT) {
            openFollowUpVisit(TestDao.getMember((view.tag as CommonPersonObjectClient).caseId))
        }
    }

    protected open fun openProfile(client: CommonPersonObjectClient?) = Unit

    protected open fun openFollowUpVisit(client: CommonPersonObjectClient?) = Unit

    override fun showNotFoundPopup(s: String) = Unit

    companion object {
        const val CLICK_VIEW_NORMAL = "click_view_normal"
        const val FOLLOW_UP_VISIT = "follow_up_visit"
    }


    protected open fun openFollowUpVisit(memberObject: MemberObject?) {
        //Implement
    }
}