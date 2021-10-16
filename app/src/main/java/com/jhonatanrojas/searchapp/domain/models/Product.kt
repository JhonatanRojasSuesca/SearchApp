package com.jhonatanrojas.searchapp.domain.models

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val originalPrice: Double,
    val seller: Seller?,
    val availableQuantity: Int,
    val soldQuantity: Int,
    val thumbnail: String,
    val pictures: List<Picture>,
    val attributes: List<Attribute>,
    var isAddCart: Boolean = false
)


data class Picture(val id:String, val url: String, val secure_url: String)

data class Attribute(val name: String, val valueName: String)