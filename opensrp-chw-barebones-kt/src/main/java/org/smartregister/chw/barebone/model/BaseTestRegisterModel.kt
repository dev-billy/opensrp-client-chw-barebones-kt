package org.smartregister.chw.barebone.model

import org.smartregister.chw.barebone.contract.BaseTestRegisterContract

open class BaseTestRegisterModel :
    BaseTestRegisterContract.Model {

    override fun registerViewConfigurations(viewIdentifiers: List<String?>?) = Unit

    override fun unregisterViewConfiguration(viewIdentifiers: List<String?>?) = Unit

    override fun saveLanguage(language: String?) = Unit

    override fun getLocationId(locationName: String?): String? = null

    override val initials: String?
        get() = null
}