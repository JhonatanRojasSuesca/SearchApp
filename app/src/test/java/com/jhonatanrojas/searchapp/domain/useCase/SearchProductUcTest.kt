package com.jhonatanrojas.searchapp.domain.useCase

import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.repositoryInterface.SearchProductRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

/**
 * Created by JHONATAN ROJAS on 12/10/2021.
 */
class SearchProductUcTest : BaseTest() {
    private val searchProductRepository: SearchProductRepository = mock()

    private lateinit var searchProductUC: SearchProductUC

    @Before
    fun setUp() {
        searchProductUC = SearchProductUC(
            searchProductRepository
        )
    }

    @Test
    fun successCaseUseSearch() {
        runBlocking {
            val productsDomain: ProductsDomain = mock()
            val productResults: ProductResults = mock()
            val listProductsDomain: ArrayList<ProductResults> = arrayListOf()
            listProductsDomain.add(productResults)
            `when`(productsDomain.productsSearch).thenReturn(listProductsDomain)
            `when`(productResults.id).thenReturn("123")
            `when`(searchProductRepository.searchProduct("silla", 0, 30)).thenReturn(
                flowOf(productsDomain)
            )

            searchProductUC.getSearchProduct("silla", 0, 30)
                .collect {
                    assertAll(it.productsSearch.first().id == "123")
                }

            verify(searchProductRepository).searchProduct("silla", 0, 30)
            verify(productsDomain).productsSearch
            verify(productResults).id

            verifyNoMoreInteractions(productsDomain, productResults)
        }
    }

    @Test
    fun errorCaseUseSearch() {
        runBlocking {
            val exception = HttpErrorCode(300, "error")
            val flow = flow<ProductsDomain> { throw exception }

            `when`(searchProductRepository.searchProduct("silla", 0, 30)).thenReturn(
                flow
            )

            searchProductUC.getSearchProduct("silla", 0, 30)
                .catch {
                    assert(it is HttpErrorCode)
                }.collect()

            verify(searchProductRepository).searchProduct("silla", 0, 30)
        }
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            searchProductRepository
        )
    }
}