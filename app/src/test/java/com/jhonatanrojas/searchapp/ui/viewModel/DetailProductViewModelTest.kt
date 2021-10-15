package com.jhonatanrojas.searchapp.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.CoroutinesTestRule
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.domain.exception.BadRequestException
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.exception.NotFoundException
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.domain.useCase.GetProductDetailUC
import com.jhonatanrojas.searchapp.ui.states.DetailState
import com.jhonatanrojas.searchapp.ui.states.SearchState
import com.jhonatanrojas.searchapp.ui.viewModels.DetailProductViewModel
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
class DetailProductViewModelTest : BaseTest() {

    private val getProductDetailUC: GetProductDetailUC = mock()
    private val mapperExceptions: Mapper<DomainException, Int> =mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val detailProductViewModel =
        DetailProductViewModel(
            getProductDetailUC,
            mapperExceptions
        )

    @Before
    fun setUp() {
        reset(
            getProductDetailUC,
            mapperExceptions
        )
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            getProductDetailUC,
            mapperExceptions
        )
    }

    @Test
    fun getDetailProductSuccess() = runBlockingTest {
        val product: Product = mock()
        val result = arrayListOf<DetailState>()
        val jop = launch {
            detailProductViewModel.model.toList(result)
        }
        `when`(product.id).thenReturn("123")
        `when`(product.title).thenReturn("Arroz")
        `when`(product.thumbnail).thenReturn("www.path.com")
        `when`(getProductDetailUC.getProductById("id"))
            .thenReturn(flowOf(product))

        detailProductViewModel.getProductById("id")

        assertAll(
            result.isNotEmpty(),
            result[0] == DetailState.HideLoading,
            result[1] == DetailState.Loading,
            result[2] == DetailState.HideLoading,
            detailProductViewModel.product.value?.id == "123",
            detailProductViewModel.product.value?.title == "Arroz",
            detailProductViewModel.product.value?.thumbnail == "www.path.com"
        )

        verify(getProductDetailUC).getProductById("id")
        verify(product).id
        verify(product).title
        verify(product).thumbnail
        verifyNoMoreInteractions(getProductDetailUC, product)
        jop.cancel()
    }

    @Test
    fun detailProductHttpErrorCode() = runBlockingTest {
        val exception = HttpErrorCode(0, "message_error")
        val flow = flow<Product> {
            throw exception
        }
        val result = arrayListOf<DetailState>()
        val jop = launch {
            detailProductViewModel.model.toList(result)
        }

        `when`(getProductDetailUC.getProductById("id"))
            .thenReturn(flow)

        detailProductViewModel.getProductById("id")

        assertAll(
            result.isNotEmpty(),
            result[0] == DetailState.HideLoading,
            result[1] == DetailState.Loading,
            result[2] == DetailState.HideLoading,
            result[3] == DetailState.ShowHttpError("message_error")
        )

        verify(getProductDetailUC).getProductById("id")
        jop.cancel()
    }

    @Test
    fun detailProductBadRequestException() = runBlockingTest {
        val exception = BadRequestException
        val flow = flow<Product> {
            throw exception
        }
        val result = arrayListOf<DetailState>()
        val jop = launch {
            detailProductViewModel.model.toList(result)
        }

        `when`(getProductDetailUC.getProductById("id"))
            .thenReturn(flow)

        detailProductViewModel.getProductById("id")

        assertAll(
            result.isNotEmpty(),
            result[0] == DetailState.HideLoading,
            result[1] == DetailState.Loading,
            result[2] == DetailState.HideLoading,
            result[3] == DetailState.ShowErrorResource(R.string.error_missing_params)
        )

        verify(getProductDetailUC).getProductById("id")
        jop.cancel()
    }


        @Test
        fun detailProductOtherException() = runBlockingTest {
            val exception = NotFoundException
            val flow = flow<Product> {
                throw exception
            }
            val result = arrayListOf<DetailState>()
            val jop = launch {
                detailProductViewModel.model.toList(result)
            }

            `when`(getProductDetailUC.getProductById("id"))
                .thenReturn(flow)
            `when`(mapperExceptions(exception))
                .thenReturn(R.string.error_some_wrong)

            detailProductViewModel.getProductById("id")

            assertAll(
                result.isNotEmpty(),
                result[0] == DetailState.HideLoading,
                result[1] == DetailState.Loading,
                result[2] == DetailState.HideLoading,
                result[3] == DetailState.ShowErrorResource(R.string.error_some_wrong)
            )

            verify(getProductDetailUC).getProductById("id")
            verify(mapperExceptions).invoke(exception)
            jop.cancel()
        }
}
