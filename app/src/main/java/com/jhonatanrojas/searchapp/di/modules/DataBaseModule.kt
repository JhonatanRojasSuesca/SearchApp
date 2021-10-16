package com.jhonatanrojas.searchapp.di.modules

import android.app.Application
import androidx.room.Room
import com.jhonatanrojas.searchapp.data.modelLocal.CartDao
import com.jhonatanrojas.searchapp.di.dataBase.CartDB
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
val cartDB = module {
    fun provideDataBase(application: Application): CartDB {
        return Room.databaseBuilder(application, CartDB::class.java, "CARTDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(dataBase: CartDB): CartDao {
        return dataBase.cartDao
    }
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }

}