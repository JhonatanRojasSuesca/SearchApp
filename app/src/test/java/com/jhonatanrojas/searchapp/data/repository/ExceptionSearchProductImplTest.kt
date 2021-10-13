package com.jhonatanrojas.searchapp.data.repository

import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.data.repositroy.exceptionImpl.ExceptionSearchProductImpl
import com.jhonatanrojas.searchapp.domain.exception.BadRequestException
import com.jhonatanrojas.searchapp.domain.exception.InternalErrorException
import com.jhonatanrojas.searchapp.domain.exception.NotFoundException
import com.jhonatanrojas.searchapp.domain.exception.UnknownError
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import retrofit2.HttpException
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

/**
 * Created by JHONATAN ROJAS on 12/10/2021.
 */
class ExceptionSearchProductImplTest : BaseTest() {

    private val httpException: HttpException = mock()
    private val response: Response<String> = mock()
    private val errorBody: ResponseBody = mock()

    private lateinit var exceptionReturnsSupplySortProductsImplTest: ExceptionSearchProductImpl

    @Before
    fun setUp() {
        this.exceptionReturnsSupplySortProductsImplTest =
            ExceptionSearchProductImpl()
    }

    @Test
    fun `Manage Http bad request`() {
        `when`(httpException.code()).thenReturn(HttpsURLConnection.HTTP_BAD_REQUEST)

        val domainException =
            this.exceptionReturnsSupplySortProductsImplTest.manageError(httpException)

        verify(httpException, Mockito.times(2)).code()
        assert(domainException is BadRequestException)
        verifyNoMoreInteractions(httpException)
    }

    @Test
    fun `Manage Http not fount`() {
        `when`(httpException.code()).thenReturn(HttpsURLConnection.HTTP_NOT_FOUND)

        val domainException =
            this.exceptionReturnsSupplySortProductsImplTest.manageError(httpException)

        verify(httpException, Mockito.times(2)).code()
        assert(domainException is NotFoundException)
    }

    @Test
    fun `Manage Http internal error`() {
        `when`(httpException.code()).thenReturn(HttpsURLConnection.HTTP_INTERNAL_ERROR)

        val domainException =
            this.exceptionReturnsSupplySortProductsImplTest.manageError(httpException)

        verify(httpException, Mockito.times(2)).code()
        assert(domainException is InternalErrorException)
    }

    @Test
    fun `Manage other exception`() {
        val domainException =
            this.exceptionReturnsSupplySortProductsImplTest.manageError(Throwable())

        assert(domainException is UnknownError)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(response, errorBody)
    }
}
