package com.jhonatanrojas.searchapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.domain.exception.BadRequestException
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.exception.InternalErrorException
import com.jhonatanrojas.searchapp.domain.useCase.SearchProductUC
import com.jhonatanrojas.searchapp.ui.states.SearchState
import com.jhonatanrojas.searchapp.utils.Mapper
import com.jhonatanrojas.searchapp.utils.handleViewModelExceptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class SearchProductViewModel(
    private val searchProductUC: SearchProductUC,
    private val mapperExceptions: Mapper<DomainException, Int>
) : ViewModel() {

    private val _model = MutableStateFlow<SearchState>(SearchState.HideLoading)
    val model: StateFlow<SearchState>
        get() = _model


    fun searchProduct(reference: String) = viewModelScope.launch {
        searchProductUC.getSearchProduct(reference)
            .onStart {
                _model.value = SearchState.Loading
            }
            .onCompletion {
                _model.value = SearchState.HideLoading
            }
            .handleViewModelExceptions { domainException ->
                _model.value = getStateFromException(domainException)
            }
           .collect { response ->
               _model.value = SearchState.Success(response)
            }
    }

    private fun getStateFromException(
        domainException: DomainException
    ): SearchState {
        return when (domainException) {
            is BadRequestException ->
                SearchState.ShowErrorResource(R.string.error_missing_params)
            is HttpErrorCode ->
                SearchState.ShowHttpError(domainException.code, domainException.message)
            else ->
                SearchState.ShowErrorResource(mapperExceptions(domainException))
        }
    }
}