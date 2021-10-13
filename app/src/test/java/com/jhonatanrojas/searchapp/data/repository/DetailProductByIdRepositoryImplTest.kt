package com.jhonatanrojas.searchapp.data.repository

import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.data.repositroy.DetailProductByIdRepositoryImpl
import com.jhonatanrojas.searchapp.data.repositroy.exceptionImpl.ExceptionSearchProductImpl
import com.jhonatanrojas.searchapp.data.response.ProductResponse
import com.jhonatanrojas.searchapp.domain.exception.NotFoundException
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DetailProductByIdRepository
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
class DetailProductByIdRepositoryImplTest : BaseTest() {
    private val searchProductApi: SearchProductApi = mock()
    private val exceptionSearchProductImpl = ExceptionSearchProductImpl()
    private val mapDomainProduct: Mapper<
        ProductResponse,
        Product> = mock()

    private lateinit var detailProductByIdRepository: DetailProductByIdRepository

    @Before
    fun setUp() {
        detailProductByIdRepository = DetailProductByIdRepositoryImpl(
            searchProductApi,
            exceptionSearchProductImpl,
            mapDomainProduct
        )
    }

    @Test
    fun successSearchProductTest() {
        runBlocking {
            val productResponse: ProductResponse = mock()
            val product: Product = mock()

            `when`(searchProductApi.getProductDetail("id")).thenReturn(
                productResponse
            )
            `when`(product.id).thenReturn("123")
            `when`(mapDomainProduct.invoke(productResponse)).thenReturn(
                product
            )

            detailProductByIdRepository.getProductById("id")
                .collect {
                    assertAll(it.id == "123")
                }

            verify(searchProductApi).getProductDetail("id")
            verify(product).id
            verify(mapDomainProduct).invoke(productResponse)

            verifyNoMoreInteractions(productResponse, product)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun returnsSupplyError() {
        runBlockingTest {
            val exception: HttpException = mock()

            `when`(searchProductApi.getProductDetail("id")).thenThrow(
                exception
            )
            `when`(exception.code()).thenReturn(404)

            detailProductByIdRepository.getProductById("id")
                .catch {
                    assertAll(it is NotFoundException)
                }.collect()

            verify(searchProductApi).getProductDetail("id")
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
            mapDomainProduct
        )
    }
}