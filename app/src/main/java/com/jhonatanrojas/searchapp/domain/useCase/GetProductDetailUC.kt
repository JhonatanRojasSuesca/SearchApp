package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DetailProductByIdRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
class GetProductDetailUC(
    private val detailProductByIdRepository: DetailProductByIdRepository
) {
    /**
     * caso de uso para traer el detalle del producto por el id y se retorna el flow
     */
    fun getProductById(id: String): Flow<Product> {
        return detailProductByIdRepository.getProductById(id)
    }
}