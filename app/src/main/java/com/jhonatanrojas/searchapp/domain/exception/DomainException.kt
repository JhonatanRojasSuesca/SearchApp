package com.jhonatanrojas.searchapp.domain.exception

import java.io.IOException

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

open class DomainException(override val message: String = "") : Throwable(message)
object NotFoundException : DomainException()
object BadRequestException : DomainException()
object InternalErrorException : DomainException()
object NoConnectivityDomainException : DomainException()
object TimeOutException : DomainException()
object ParseException : DomainException()
data class HttpErrorCode(val code: Int, override val message: String) : DomainException()
object NoConnectivityException : IOException()
object UnknownError : DomainException()