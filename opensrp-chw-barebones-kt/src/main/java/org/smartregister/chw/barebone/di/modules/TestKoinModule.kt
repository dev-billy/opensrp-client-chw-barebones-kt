package org.smartregister.chw.barebone.di.modules

import id.zelory.compressor.Compressor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.smartregister.Context
import org.smartregister.CoreLibrary
import org.smartregister.chw.barebone.TestLibrary
import org.smartregister.chw.barebone.di.modules.TestKoinModule.appModule
import org.smartregister.repository.TaskNotesRepository
import org.smartregister.repository.TaskRepository
import org.smartregister.sync.ClientProcessorForJava
import org.smartregister.sync.helper.ECSyncHelper

/**
 * This is used internally to provide modules for testing, it includes:
 * [appModule], [repositoryModule] and [providerModule]
 */
object TestKoinModule {
    @JvmField
    val appModule = module {
        single { TestLibrary.getInstance() }
        single { ECSyncHelper.getInstance(androidApplication()) }
        single { Context.getInstance() }
        single { ClientProcessorForJava.getInstance(androidApplication()) }
        single { Compressor.getDefault(androidApplication()) }
        single { CoreLibrary.init(get()) }
        single { TaskRepository(get()) }
        single { TaskNotesRepository() }
    }

}