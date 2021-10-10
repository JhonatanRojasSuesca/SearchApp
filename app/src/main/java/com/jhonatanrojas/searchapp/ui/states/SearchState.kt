package com.jhonatanrojas.searchapp.ui.states

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
sealed class SearchState {

    object Loading : SearchState()
    object HideLoading : SearchState()
    data class ShowErrorResource(val resource: Int) : SearchState()
    data class ShowHttpError(val message: String) : SearchState()
}