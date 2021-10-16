package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.repositoryInterface.LocalCartRepository

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
class ManagementProductLocalCartUC(
    private val localCartRepository: LocalCartRepository
) {

    fun getProductCart() = localCartRepository.getProductsCart()

    suspend fun getListIdsCart(): List<String> {
        return localCartRepository.getListIdsCart()
    }

    suspend fun insertProductCart(productResults: ProductResults) {
        localCartRepository.insertProduct(productResults)
    }

    suspend fun productIsAddCart(id: String): Boolean {
        val listResults = localCartRepository.productIsAddCart(id)
        return listResults.isNullOrEmpty().not()
    }

    suspend fun deleteProductCart(productResults: ProductResults) =
        localCartRepository.deleteItem(productResults)
}
