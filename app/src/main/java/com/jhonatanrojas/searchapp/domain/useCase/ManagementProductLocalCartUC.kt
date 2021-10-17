package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.repositoryInterface.LocalCartRepository

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
class ManagementProductLocalCartUC(
    private val localCartRepository: LocalCartRepository
) {
    /**
     * metodo que trae la lista de productos agragados al carrito
     */
    fun getProductCart() = localCartRepository.getProductsCart()

    /**
     * metodo del caso de uso que trae la lista de strings para validacion productos en el carrito
     */
    suspend fun getListIdsCart(): List<String> {
        return localCartRepository.getListIdsCart()
    }

    /**
     * metodo del caso de uso que inserta productos al carrito desde los viewModels de search y del detail
     */
    suspend fun insertProductCart(productResults: ProductResults) {
        localCartRepository.insertProduct(productResults)
    }

    /**
     * metodo del caso de uso que elimina por completo el carrito desde el fragment del cart
     */
    suspend fun deleteAllCart() {
        localCartRepository.deleteAllCart()
    }

    /**
     * metodo del caso de uso que valida si existe el producto en la base de datos y dependiendo de la respuesta retorna true o false
     */
    suspend fun productIsAddCart(id: String): Boolean {
        val listResults = localCartRepository.productIsAddCart(id)
        return listResults.isNullOrEmpty().not()
    }
    /**
     * metodo del caso de uso que elimina el producto de la base de datos
     */
    suspend fun deleteProductCart(productResults: ProductResults) =
        localCartRepository.deleteItem(productResults)
}
