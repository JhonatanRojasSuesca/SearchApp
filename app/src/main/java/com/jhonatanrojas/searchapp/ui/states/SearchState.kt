package com.jhonatanrojas.searchapp.ui.states

import com.jhonatanrojas.searchapp.domain.models.Product

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
sealed class SearchState {

    object Loading : SearchState()
    object HideLoading : SearchState()
    data class Success(val supplyManualSearchProduct: List<Product>) :
        SearchState()
    data class ShowErrorResource(val resource: Int) : SearchState()
    data class ShowHttpError(val code: Int, val message: String) : SearchState()
}