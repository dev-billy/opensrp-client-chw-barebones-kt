package org.smartregister.chw.barebone.di.modules

import id.zelory.compressor.Compressor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.smartregister.Context
import org.smartregister.chw.barebone.TestLibrary
import org.smartregister.repository.TaskNotesRepository
import org.smartregister.repository.TaskRepository
import org.smartregister.sync.ClientProcessorForJava
import org.smartregister.sync.helper.ECSyncHelper

/**
 * This provide modules used in Dependency Injection by the Koin library.
 */
object MainKoinModule {
    /**
     * [appModule] provides common modules used within the application
     */
    @JvmField
    val appModule = module {
        single { TestLibrary.getInstance() }
        single { Context.getInstance() }
        single { ClientProcessorForJava.getInstance(androidApplication()) }
        single { Compressor.getDefault(androidApplication()) }
        single { ECSyncHelper.getInstance(androidApplication()) }
        single { TaskRepository(get()) }
        single { TaskNotesRepository() }
    }

    /**
     * [appModule] provides common modules used within the application to be loaded if Koin has already been started by other modules
     */
    @JvmField
    val appModuleLoadedIfKoinAlreadyStarted = module {
        single { TestLibrary.getInstance() }
    }
}