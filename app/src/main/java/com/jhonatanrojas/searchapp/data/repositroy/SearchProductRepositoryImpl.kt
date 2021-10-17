package com.jhonatanrojas.searchapp.data.repositroy

import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DomainExceptionRepository
import com.jhonatanrojas.searchapp.domain.repositoryInterface.SearchProductRepository
import com.jhonatanrojas.searchapp.utils.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
class SearchProductRepositoryImpl(
    private val searchProductApi: SearchProductApi,
    private val exceptionSearchProductImpl: DomainExceptionRepository,
    private val mapSearchProduct: Mapper<
        SearchProductsResponse,
        ProductsDomain>
) : SearchProductRepository {

    /**
     * Este Metodo trae del api la lista de productos referenciados con el texto ingresado por el usuario
     * se envia por parametro el text de referencia y el paginado y el limite de busqueda
     * y ejecuta los mapper que pasa del SearchProductsResponse a ProductsDomain modelo de dominio para la vista del search
     * asi mismo el mapper de exception que se transforma a exception controladas de DomainException
     */
    override fun searchProduct(search: String, offset: Int, limit: Int): Flow<ProductsDomain> {
        return flow {
            searchProductApi.searchProducts(
                search,
                offset,
                limit
            ).run { emit(mapSearchProduct(this)) }
        }.catch { throw exceptionSearchProductImpl.manageError(it) }
    }
}