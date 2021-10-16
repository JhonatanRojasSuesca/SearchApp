package com.jhonatanrojas.searchapp.domain.repositoryInterface

import com.jhonatanrojas.searchapp.domain.models.ProductResults
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
interface LocalCartRepository {
    fun getProductsCart(): Flow<List<ProductResults>>
    suspend fun getListIdsCart(): List<String>
    suspend fun insertProduct(productResults: ProductResults)
    suspend fun productIsAddCart(id: String) : List<ProductResults>
    suspend fun deleteItem(productResults: ProductResults): Int
    suspend fun deleteAllCart()

}