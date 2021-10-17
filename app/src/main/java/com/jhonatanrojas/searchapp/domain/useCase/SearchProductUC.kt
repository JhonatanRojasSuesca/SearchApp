package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.repositoryInterface.SearchProductRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class SearchProductUC(
    private val searchProductRepository: SearchProductRepository
) {
    /**
     * metodo del caso de uso que trae la busqueda ejecutada desde el search con parametros
     */
    fun getSearchProduct(search: String, offset: Int, limit: Int): Flow<ProductsDomain> {
        return searchProductRepository.searchProduct(
            search,
            offset,
            limit
        )
    }
}