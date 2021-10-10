package com.jhonatanrojas.searchapp.domain.repositoryInterface

import com.jhonatanrojas.searchapp.domain.exception.DomainException

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
interface DomainExceptionRepository {
    fun manageError(error: Throwable): DomainException
}
