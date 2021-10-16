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

    fun insertProductCart(productResults: ProductResults) {
        localCartRepository.insertProduct(productResults)
    }

    fun deleteProductCart(productResults: ProductResults) =
        localCartRepository.deleteItem(productResults)
}
