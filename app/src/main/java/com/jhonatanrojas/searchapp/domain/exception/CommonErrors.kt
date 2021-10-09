package com.jhonatanrojas.searchapp.domain.exception

import com.google.gson.JsonSyntaxException
import com.squareup.moshi.JsonDataException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Created By Juan Felipe Arango on 16/09/20
 * Copyright ©2020 Merqueo. All rights reserved
 */
open class CommonErrors {

    fun manageException(throwable: Throwable): DomainException {
        return manageJavaErrors(throwable)
    }

    fun manageJavaErrors(throwable: Throwable): DomainException {
        return when (throwable) {
            is SocketTimeoutException -> TimeOutException
            is ConnectException -> InternalErrorException
            else -> manageParsingExceptions(throwable)
        }
    }

    fun manageParsingExceptions(throwable: Throwable): DomainException {
        return when (throwable) {
            is JsonDataException -> ParseException
            is JsonSyntaxException -> ParseException
            else -> manageOtherException(throwable)
        }
    }

    fun manageOtherException(throwable: Throwable): DomainException {
        return when (throwable) {
            is NoConnectivityException -> NoConnectivityDomainException
            else -> UnknownError
        }
    }
}
