package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.repository.SearchProductRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class SearchProductUC(
    private val searchProductRepository: SearchProductRepository
) {
    fun getSearchProduct(reference: String): Flow<List<Product>> {
        return searchProductRepository.searchProduct(reference)
    }
}