package com.jhonatanrojas.searchapp.data.repositroy

import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.repository.DomainExceptionRepository
import com.jhonatanrojas.searchapp.domain.repository.SearchProductRepository
import com.jhonatanrojas.searchapp.utils.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
class SearchProductRepositoryImpl(
    private val searchProductApi: SearchProductApi,
    private val exceptionSupplyManualSearchProductImpl: DomainExceptionRepository,
    private val mapSearchProduct: Mapper<
        SearchProductsResponse,
        ProductsDomain>
) : SearchProductRepository {

    override fun searchProduct(reference: String): Flow<List<Product>> {
        return flow { }
    }
}