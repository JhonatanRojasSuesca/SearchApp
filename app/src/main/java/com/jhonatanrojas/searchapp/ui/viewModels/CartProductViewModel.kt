package com.jhonatanrojas.searchapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.useCase.ManagementProductLocalCartUC
import com.jhonatanrojas.searchapp.ui.states.CartState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
class CartProductViewModel(
    private val managementProductLocalCartUC: ManagementProductLocalCartUC
) : ViewModel() {
    val productsList: MutableLiveData<List<ProductResults>> = MutableLiveData()
    private val _model = MutableStateFlow<CartState>(CartState.HideLoading)
    val model: StateFlow<CartState>
        get() = _model

    /**
     * trae todos los productos agregados al carrito maneja los estados del loading y agrega los productos al live data
     */
    fun getProductsCart() = viewModelScope.launch {
        managementProductLocalCartUC.getProductCart()
            .onStart {
                _model.value = CartState.Loading
            }
            .catch { _model.value = CartState.HideLoading }
            .onCompletion {
                _model.value = CartState.HideLoading
            }
            .collect { response ->
                productsList.postValue(response)
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

    /**
     * elimina todos los productos del carrito
     */
    fun deleteAllCart() {
        viewModelScope.launch {
            managementProductLocalCartUC.deleteAllCart()
        }
    }
}
