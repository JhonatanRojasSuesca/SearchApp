package com.jhonatanrojas.searchapp.ui.states

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
sealed class DetailState {
    object Loading : DetailState()
    object HideLoading : DetailState()
    data class ShowErrorResource(val resource: Int) : DetailState()
    data class ShowHttpError(val message: String) : DetailState()
}