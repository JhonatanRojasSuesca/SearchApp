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
    private val exceptionSearchProductImpl: DomainExceptionRepository,
    private val mapDomainProduct: Mapper<
        ProductResponse,
        Product>
) : DetailProductByIdRepository {

    /**
     * Este Metodo del api el detalle del producto con el id pasado por parametro
     * y ejecuta los mapper que pasa del ProductResponse a Product modelo de dominio para la vista del detalle
     * asi mismo el mapper de exception que se tranbsforma a exception controladas de DomainException
     */
    override fun getProductById(id: String): Flow<Product> {
        return flow {
            searchProductApi.getProductDetail(id)
                .run { emit(mapDomainProduct(this)) }
        }.catch { throw exceptionSearchProductImpl.manageError(it) }
    }
}