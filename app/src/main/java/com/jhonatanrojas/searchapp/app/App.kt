package com.jhonatanrojas.searchapp.app

import android.app.Application
import com.jhonatanrojas.searchapp.di.koin.repositoryModule
import com.jhonatanrojas.searchapp.di.koin.retrofitModule
import com.jhonatanrojas.searchapp.di.koin.useCaseModule
import com.jhonatanrojas.searchapp.di.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    retrofitModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}