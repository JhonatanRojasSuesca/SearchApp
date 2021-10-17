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

    /**
     * metodo del search para traer los resultados de la busqueda
     * con los estados para el manejo del loading yde errores
     * tambien envia el live data de la lista de los productos buscados
     */

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
                    productsList.value = response.productsSearch
                    validateProductsInCart()
                }
        }
    }

    /**
     *valida que los productos enlistados en la busqueda ya estene dentro del carrito para ocupar el icono de eliminar del carrito
     */
    fun validateProductsInCart() {
        viewModelScope.launch {
            val listCart = managementProductLocalCartUC.getListIdsCart()
            productsList.value?.let {
                it.forEach { productResults ->
                    productResults.apply {
                        isAddCart = false
                    }
                }
            }
            if (listCart.isNullOrEmpty().not()) {
                listCart.forEach { id ->
                    productsList.value?.find { it.id == id }
                        .apply {
                            this?.let {
                                isAddCart = true
                            }
                        }
                }
            }
            if (productsList.value.isNullOrEmpty().not()) {
                productsList.postValue(productsList.value)
            }
        }
    }

    /**
     * trae el estado con la exception causada y tener el manejo de los errores con los recursos y darle al usuario un error reconocible
     */
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

    /**
     *este metodo sirve para la validacion de si el usuario necesita mas items con la misma busqueda referenciada
     * y maneja la paginacion
     */
    fun onLoadMoreData(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (!isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }
        if (isDownloading.not()) offSet += PAGE_SIZE
        searchProduct()
    }

    /**
     * valida si las condiciones para poder ejecutar la busqueda por la misma referencia
     */
    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && firstVisibleItemPosition >= 0
            && totalItemCount >= PAGE_SIZE
    }

    /**
     * agrega el producto al carrito
     */
    fun addToCart(productResults: ProductResults) {
        viewModelScope.launch {
            managementProductLocalCartUC.insertProductCart(productResults)
        }
    }
    /**
     * elimina el producto al carrito
     */
    fun deleteProductCart(productResults: ProductResults) {
        viewModelScope.launch {
            managementProductLocalCartUC.deleteProductCart(productResults)
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}