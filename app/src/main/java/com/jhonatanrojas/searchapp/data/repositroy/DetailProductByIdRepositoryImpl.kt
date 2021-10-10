package com.jhonatanrojas.searchapp.data.repositroy

import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.data.response.ProductResponse
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DetailProductByIdRepository
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DomainExceptionRepository
import com.jhonatanrojas.searchapp.utils.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
class DetailProductByIdRepositoryImpl(
    private val searchProductApi: SearchProductApi,
    private val exceptionSupplyManualSearchProductImpl: DomainExceptionRepository,
    private val mapDomainProduct: Mapper<
        ProductResponse,
        Product>
) : DetailProductByIdRepository {

    override fun getProductById(id: String): Flow<Product> {
        return flow {
            searchProductApi.getProductDetail(id)
                .run { emit(mapDomainProduct(this)) }
        }.catch { throw exceptionSupplyManualSearchProductImpl.manageError(it) }
    }
}