package com.jhonatanrojas.searchapp.data.modelLocal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
@Dao
interface CartDao {
    @Query("select * from cartTable")
    fun getCart(): Flow<List<CartProductModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCart(addProduct: CartProductModel)

    @Delete
    fun deleteItemCard(deleteUser: CartProductModel): Int
}