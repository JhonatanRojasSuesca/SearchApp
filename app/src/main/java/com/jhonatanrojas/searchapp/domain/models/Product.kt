package com.jhonatanrojas.searchapp.domain.models

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val original_price: Double,
    val seller: Seller?,
    val available_quantity: Int,
    val sold_quantity: Int,
    val thumbnail: String,
    val pictures: List<Picture>,
    val attributes: List<Attribute>
)


data class Picture(val id:String, val url: String, val secure_url: String)

data class Attribute(val name: String, val valueName: String)