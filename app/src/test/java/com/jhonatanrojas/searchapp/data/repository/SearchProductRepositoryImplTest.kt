package com.jhonatanrojas.searchapp.data.repository

import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.data.repositroy.SearchProductRepositoryImpl
import com.jhonatanrojas.searchapp.data.repositroy.exceptionImpl.ExceptionSearchProductImpl
import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import com.jhonatanrojas.searchapp.domain.exception.NotFoundException
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.repositoryInterface.SearchProductRepository
import com.jhonatanrojas.searchapp.utils.Mapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.kotlin.times
import retrofit2.HttpException

/**
 * Created by JHONATAN ROJAS on 12/10/2021.
 */
class SearchProductRepositoryImplTest : BaseTest() {

    private val searchProductApi: SearchProductApi = mock()
    private val exceptionSearchProductImpl = ExceptionSearchProductImpl()
    private val mapSearchProduct: Mapper<
        SearchProductsResponse,
        ProductsDomain> = mock()

    private lateinit var searchProductRepository: SearchProductRepository

    @Before
    fun setUp() {
        searchProductRepository = SearchProductRepositoryImpl(
            searchProductApi,
            exceptionSearchProductImpl,
            mapSearchProduct
        )
    }

    @Test
    fun successSearchProductTest() {
        runBlocking {
            val searchProductResponse: SearchProductsResponse = mock()
            val productsDomain: ProductsDomain = mock()
            val productResults: ProductResults = mock()
            `when`(searchProductApi.searchProducts("silla", 0, 30)).thenReturn(
                searchProductResponse
            )
            `when`(productResults.id).thenReturn("123")
            `when`(productsDomain.productsSearch).thenReturn(listOf(productResults))
            `when`(mapSearchProduct.invoke(searchProductResponse)).thenReturn(
                productsDomain
            )

            searchProductRepository.searchProduct("silla", 0, 30)
                .collect {
                    assertAll(it.productsSearch[0].id == "123")
                }

            verify(searchProductApi).searchProducts("silla", 0, 30)
            verify(productsDomain).productsSearch
            verify(productResults).id
            verify(mapSearchProduct).invoke(searchProductResponse)

            verifyNoMoreInteractions(searchProductResponse, productsDomain, productResults)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun returnsSupplyError() {
        runBlockingTest {
            val exception: HttpException = mock()

            `when`(searchProductApi.searchProducts("silla", 0, 30)).thenThrow(
                exception
            )
            `when`(exception.code()).thenReturn(404)

            searchProductRepository.searchProduct("silla", 0, 30)
                .catch {
                    assertAll(it is NotFoundException)
                }.collect()

            verify(searchProductApi).searchProducts("silla", 0, 30)
            verify(exception, times(2)).code()

            verifyNoMoreInteractions(
                exception
            )
        }
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            searchProductApi,
            mapSearchProduct
        )
    }
}