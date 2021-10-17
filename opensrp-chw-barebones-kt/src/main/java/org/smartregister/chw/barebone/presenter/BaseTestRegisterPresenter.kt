package org.smartregister.chw.barebone.presenter

import org.smartregister.chw.barebone.contract.BaseTestRegisterContract
import java.lang.ref.WeakReference

open class BaseTestRegisterPresenter(
    view: BaseTestRegisterContract.View,
    protected var model: BaseTestRegisterContract.Model
) : BaseTestRegisterContract.Presenter {

    private var viewReference = WeakReference(view)

    override fun getView() = viewReference.get()

    override fun registerViewConfigurations(list: List<String>?) = Unit

    override fun unregisterViewConfiguration(list: List<String>?) = Unit

    override fun onDestroy(b: Boolean) = Unit

    override fun updateInitials() = Unit

}