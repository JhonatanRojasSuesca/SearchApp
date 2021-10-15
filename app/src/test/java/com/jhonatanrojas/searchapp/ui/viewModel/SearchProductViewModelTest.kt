package com.jhonatanrojas.searchapp.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.CoroutinesTestRule
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.domain.exception.BadRequestException
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.exception.NotFoundException
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.useCase.SearchProductUC
import com.jhonatanrojas.searchapp.ui.states.SearchState
import com.jhonatanrojas.searchapp.ui.viewModels.SearchProductViewModel
import com.jhonatanrojas.searchapp.utils.Mapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify

/**
 * Created by JHONATAN ROJAS on 14/10/2021.
 */
@ExperimentalCoroutinesApi
class SearchProductViewModelTest : BaseTest() {

    private val searchProductUC: SearchProductUC = mock()
    private val mapperExceptions: Mapper<DomainException, Int> = mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val searchProductViewModel =
        SearchProductViewModel(
            searchProductUC,
            mapperExceptions
        )

    @Before
    fun setUp() {
        reset(
            searchProductUC,
            mapperExceptions
        )
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            searchProductUC,
            mapperExceptions
        )
    }

    @Test
    fun getSearchProductSuccess() = runBlockingTest {
        val productsDomain: ProductsDomain = mock()
        val productResults: ProductResults = mock()
        val result = arrayListOf<SearchState>()
        searchProductViewModel.textSearch = "silla"
        searchProductViewModel.isDownloading = false
        val jop = launch {
            searchProductViewModel.model.toList(result)
        }
        `when`(productResults.id).thenReturn("123")
        `when`(productResults.title).thenReturn("Arroz")
        `when`(productResults.thumbnail).thenReturn("www.path.com")
        `when`(productsDomain.productsSearch).thenReturn(listOf(productResults))
        `when`(searchProductUC.getSearchProduct("silla", 0, 30))
            .thenReturn(flowOf(productsDomain))

        searchProductViewModel.searchProduct()

        assertAll(
            result.isNotEmpty(),
            result[0] == SearchState.HideLoading,
            result[1] == SearchState.Loading,
            result[2] == SearchState.HideLoading,
            searchProductViewModel.productsList.value?.first()?.id == "123",
            searchProductViewModel.productsList.value?.first()?.title == "Arroz",
            searchProductViewModel.productsList.value?.first()?.thumbnail == "www.path.com"
        )

        verify(searchProductUC).getSearchProduct("silla", 0, 30)
        verify(productResults).id
        verify(productResults).title
        verify(productResults).thumbnail
        verify(productsDomain).productsSearch
        verifyNoMoreInteractions(productsDomain, productResults)
        jop.cancel()
    }

    @Test
    fun searchProductHttpErrorCode() = runBlockingTest {
        val exception = HttpErrorCode(0, "message_error")
        searchProductViewModel.textSearch = "silla"
        val flow = flow<ProductsDomain> {
            throw exception
        }
        val result = arrayListOf<SearchState>()
        val jop = launch {
            searchProductViewModel.model.toList(result)
        }

        `when`(searchProductUC.getSearchProduct("silla", 0, 30))
            .thenReturn(flow)

        searchProductViewModel.searchProduct()

        assertAll(
            result.isNotEmpty(),
            result[0] == SearchState.HideLoading,
            result[1] == SearchState.Loading,
            result[2] == SearchState.HideLoading,
            result[3] == SearchState.ShowHttpError("message_error")
        )

        verify(searchProductUC).getSearchProduct("silla", 0, 30)
        jop.cancel()
    }

    @Test
    fun searchProductBadRequestException() = runBlockingTest {
        val exception = BadRequestException
        searchProductViewModel.textSearch = "silla"
        val flow = flow<ProductsDomain> {
            throw exception
        }
        val result = arrayListOf<SearchState>()
        val jop = launch {
            searchProductViewModel.model.toList(result)
        }

        `when`(searchProductUC.getSearchProduct("silla", 0, 30))
            .thenReturn(flow)

        searchProductViewModel.searchProduct()

        assertAll(
            result.isNotEmpty(),
            result[0] == SearchState.HideLoading,
            result[1] == SearchState.Loading,
            result[2] == SearchState.HideLoading,
            result[3] == SearchState.ShowErrorResource(R.string.error_missing_params)
        )

        verify(searchProductUC).getSearchProduct("silla", 0, 30)
        jop.cancel()
    }


        @Test
        fun searchProductOtherException() = runBlockingTest {
            val exception = NotFoundException
            searchProductViewModel.textSearch = "silla"
            val flow = flow<ProductsDomain> {
                throw exception
            }
            val result = arrayListOf<SearchState>()
            val jop = launch {
                searchProductViewModel.model.toList(result)
            }

            `when`(searchProductUC.getSearchProduct("silla", 0, 30))
                .thenReturn(flow)
            `when`(mapperExceptions(exception))
                .thenReturn(R.string.error_some_wrong)

            searchProductViewModel.searchProduct()

            assertAll(
                result.isNotEmpty(),
                result.isNotEmpty(),
                result[0] == SearchState.HideLoading,
                result[1] == SearchState.Loading,
                result[2] == SearchState.HideLoading,
                result[3] == SearchState.ShowErrorResource(R.string.error_some_wrong)
            )

            verify(searchProductUC).getSearchProduct("silla", 0, 30)
            verify(mapperExceptions).invoke(exception)
            jop.cancel()
        }
}
