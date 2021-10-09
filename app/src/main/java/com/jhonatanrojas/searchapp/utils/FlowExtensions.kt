package com.jhonatanrojas.searchapp.utils

import com.jhonatanrojas.searchapp.domain.exception.DomainException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

internal fun <T> Flow<T>.handleExceptions(onError: (Throwable) -> Unit): Flow<T> = flow {
    runCatching {
        collect { value -> emit(value) }
    }.onFailure(onError)
}

internal fun <T> Flow<T>.handleViewModelExceptions(onError: (DomainException) -> Unit): Flow<T> =
    flow {
        try {
            collect { value -> emit(value) }
        } catch (e: DomainException) {
            onError(e)
        }
    }
