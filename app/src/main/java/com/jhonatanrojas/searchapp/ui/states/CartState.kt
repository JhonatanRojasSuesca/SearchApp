package com.jhonatanrojas.searchapp.ui.states

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
sealed class CartState {
    object Loading : CartState()
    object HideLoading : CartState()
}