package com.jhonatanrojas.searchapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.domain.exception.BadRequestException
import com.jhonatanrojas.searchapp.domain.exception.DomainException
import com.jhonatanrojas.searchapp.domain.exception.HttpErrorCode
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.useCase.ManagementProductLocalCartUC
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
    private val mapperExceptions: Mapper<DomainException, Int>,
    private val managementProductLocalCartUC: ManagementProductLocalCartUC
) : ViewModel() {
    val productsList: MutableLiveData<List<ProductResults>> = MutableLiveData()
    private val _model = MutableStateFlow<SearchState>(SearchState.HideLoading)
    val model: StateFlow<SearchState>
        get() = _model
    var offSet = 0
    var textSearch: String = ""
    var isDownloading = false

    fun searchProduct() = viewModelScope.launch {
        if (isDownloading.not()) {
            isDownloading = true
            searchProductUC.getSearchProduct(textSearch, offSet, 30)
                .onStart {
                    _model.value = SearchState.Loading
                }
                .onCompletion {
                    _model.value = SearchState.HideLoading
                    isDownloading = false
                }
                .handleViewModelExceptions { domainException ->
                    _model.value = getStateFromException(domainException)
                }
                .collect { response ->
                    productsList.postValue(response.productsSearch)
                }
        }
    }

    private fun getStateFromException(
        domainException: DomainException
    ): SearchState {
        return when (domainException) {
            is BadRequestException ->
                SearchState.ShowErrorResource(R.string.error_missing_params)
            is HttpErrorCode ->
                SearchState.ShowHttpError(domainException.message)
            else ->
                SearchState.ShowErrorResource(mapperExceptions(domainException))
        }
    }

    fun onLoadMoreData(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (!isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }
        if (isDownloading.not()) offSet += PAGE_SIZE
        searchProduct()
    }

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && firstVisibleItemPosition >= 0
            && totalItemCount >= PAGE_SIZE
    }

    fun addToCart(productResults: ProductResults) {
        viewModelScope.launch {
            managementProductLocalCartUC.insertProductCart(productResults)
            val listTest: MutableLiveData<List<ProductResults>> = productsList

            listTest.value?.find { it.id == productResults.id }.apply {
                this?.isAddCart = true
            }
            productsList.postValue(listTest.value)
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}