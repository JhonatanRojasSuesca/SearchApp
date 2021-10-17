package com.jhonatanrojas.searchapp.domain.models

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
data class ProductResults(
    val id: String,
    val title: String,
    val price: Double,
    val seller: Seller?,
    val thumbnail: String,
    var isAddCart: Boolean = false
)