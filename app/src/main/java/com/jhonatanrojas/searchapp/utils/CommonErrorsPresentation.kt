package com.jhonatanrojas.searchapp.utils

import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.InternalErrorException
import com.jhonatanrojas.searchapp.domain.exception.NoConnectivityDomainException
import com.jhonatanrojas.searchapp.domain.exception.ParseException
import com.jhonatanrojas.searchapp.domain.exception.TimeOutException

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
fun manageErrorsToPresentation(): Mapper<DomainException, Int> = {
    when (it) {
        is TimeOutException -> R.string.error_time_out
        is InternalErrorException -> R.string.error_internal_error_exception
        is ParseException -> R.string.error_parsing_error
        is NoConnectivityDomainException -> R.string.error_internet_connection
        else -> R.string.error_some_wrong
    }
}
