package com.jhonatanrojas.searchapp.data.modelLocal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
@Dao
interface CartDao {
    @Query("select * from cartTable")
    fun getCart(): Flow<List<CartProductModel>>

    @Query("select id from cartTable")
    suspend fun getIdsCart(): List<String>

    @Query("select * from cartTable where id = :id ")
    suspend fun getProductCart(id: String): List<CartProductModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(addProduct: CartProductModel)

    @Delete
    suspend fun deleteItemCard(deleteUser: CartProductModel): Int

    @Query("DELETE FROM cartTable")
    suspend fun deleteAllChannels()
}