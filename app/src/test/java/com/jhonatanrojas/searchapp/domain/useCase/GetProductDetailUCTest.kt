package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DetailProductByIdRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

/**
 * Created by JHONATAN ROJAS on 12/10/2021.
 */
class GetProductDetailUCTest : BaseTest() {
    private val detailProductByIdRepository: DetailProductByIdRepository = mock()

    private lateinit var getProductDetailUC: GetProductDetailUC

    @Before
    fun setUp() {
        getProductDetailUC = GetProductDetailUC(
            detailProductByIdRepository
        )
    }

    @Test
    fun successCaseUseSearch() {
        runBlocking {
            val product: Product = mock()
            `when`(product.id).thenReturn("123")
            `when`(detailProductByIdRepository.getProductById("123")).thenReturn(
                flowOf(product)
            )

            getProductDetailUC.getProductById("123")
                .collect {
                    assertAll(it.id == "123")
                }

            verify(detailProductByIdRepository).getProductById("123")
            verify(product).id

            verifyNoMoreInteractions(product)
        }
    }

    @Test
    fun errorCaseUseSearch() {
        runBlocking {
            val exception = HttpErrorCode(300, "error")
            val flow = flow<Product> { throw exception }

            `when`(detailProductByIdRepository.getProductById("123")).thenReturn(
                flow
            )

            getProductDetailUC.getProductById("123")
                .catch {
                    assert(it is HttpErrorCode)
                }.collect()

            verify(detailProductByIdRepository).getProductById("123")
        }
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(
            detailProductByIdRepository
        )
    }
}