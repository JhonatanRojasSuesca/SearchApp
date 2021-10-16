package com.jhonatanrojas.searchapp.domain.repositoryInterface

import com.jhonatanrojas.searchapp.domain.models.ProductResults
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
interface LocalCartRepository {
    fun getProductsCart(): Flow<List<ProductResults>>
    fun insertProduct(productResults: ProductResults)
    fun deleteItem(productResults: ProductResults): Int
}