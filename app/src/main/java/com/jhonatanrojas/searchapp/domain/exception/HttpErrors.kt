/**
 * Created By Dairo Aguas Barraza on 5/4/2021.
 * Copyright ©2021 Merqueo. All rights reserved
 */

package com.jhonatanrojas.searchapp.domain.exception

import com.google.gson.Gson
import com.jhonatanrojas.searchapp.utils.Constants
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

object HttpErrors {
    private val httpErrors = mapOf(
        HttpsURLConnection.HTTP_BAD_REQUEST to BadRequestException,
        HttpsURLConnection.HTTP_NOT_FOUND to NotFoundException,
        HttpsURLConnection.HTTP_INTERNAL_ERROR to InternalErrorException
    )

    fun getHttpError(error: HttpException): DomainException {
        return if (httpErrors.containsKey(error.code())) {
            httpErrors.getValue(error.code())
        } else {
            HttpErrorCode(error.code(), getMessage(error).message)
        }
    }

    private fun getMessage(exception: HttpException): DomainException {
        return try {
            var jsonString = exception.response()?.errorBody()?.string()
            if (jsonString.isNullOrEmpty()) jsonString = Constants.JSON_EMPTY
            Gson().fromJson(jsonString, DomainException::class.java)
        } catch (exception_: Exception) {
            DomainException(Constants.EMPTY)
        }
    }
}
