package com.jhonatanrojas.searchapp.data.repositroy.exceptionImpl

import com.jhonatanrojas.searchapp.domain.exception.CommonErrors
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.HttpErrors.getHttpError
import com.jhonatanrojas.searchapp.domain.repository.DomainExceptionRepository
import retrofit2.HttpException

class ExceptionSearchProductImpl : CommonErrors(), DomainExceptionRepository {

    override fun manageError(error: Throwable): DomainException {
        return if (error is HttpException) {
            getHttpError(error)
        } else {
            manageException(error)
        }
    }
}
