package com.jhonatanrojas.searchapp.di.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jhonatanrojas.searchapp.data.modelLocal.CartDao
import com.jhonatanrojas.searchapp.data.modelLocal.CartProductModel

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */

@Database(entities = [CartProductModel::class], version = 1, exportSchema = false)
abstract class CartDB : RoomDatabase() {
    abstract val cartDao: CartDao
}