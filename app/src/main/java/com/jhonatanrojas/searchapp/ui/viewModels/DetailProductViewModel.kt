package com.jhonatanrojas.searchapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.domain.exception.BadRequestException
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.domain.useCase.GetProductDetailUC
import com.jhonatanrojas.searchapp.ui.states.DetailState
import com.jhonatanrojas.searchapp.utils.Mapper
import com.jhonatanrojas.searchapp.utils.handleViewModelExceptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
class DetailProductViewModel(
    private val getProductDetailUC: GetProductDetailUC,
    private val mapperExceptions: Mapper<DomainException, Int>
) : ViewModel() {
    val product: MutableLiveData<Product> = MutableLiveData()
    private val _model = MutableStateFlow<DetailState>(DetailState.HideLoading)
    val model: StateFlow<DetailState>
        get() = _model

    fun getProductById(id: String) = viewModelScope.launch {
        getProductDetailUC.getProductById(id)
            .onStart {
                _model.value = DetailState.Loading
            }
            .onCompletion {
                _model.value = DetailState.HideLoading
            }
            .handleViewModelExceptions { domainException ->
                _model.value = getStateFromException(domainException)
            }
            .collect { response ->
                product.postValue(response)
            }
    }

    private fun getStateFromException(
        domainException: DomainException
    ): DetailState {
        return when (domainException) {
            is BadRequestException ->
                DetailState.ShowErrorResource(R.string.error_missing_params)
            is HttpErrorCode ->
                DetailState.ShowHttpError(domainException.message)
            else ->
                DetailState.ShowErrorResource(mapperExceptions(domainException))
        }
    }
}