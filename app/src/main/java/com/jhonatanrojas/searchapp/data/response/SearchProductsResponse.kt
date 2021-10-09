package com.jhonatanrojas.searchapp.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
@JsonClass(generateAdapter = true)
data class SearchProductsResponse(
    @Json(name = "site_id")
    val site_id: String,
    @Json(name = "paging")
    val paging: PagingResponse,
    @Json(name = "results")
    val results: List<ProductResponse>
)

@JsonClass(generateAdapter = true)
data class PagingResponse(
    @Json(name = "total")
    val total: Int,
    @Json(name = "offset")
    val offset: Int,
    @Json(name = "limit")
    val limit: Int
)

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "price")
    val price: Double,
    @Json(name = "sale_price")
    val original_price: Double?,
    @Json(name = "seller")
    val seller: SellerResponse?,
    @Json(name = "available_quantity")
    val available_quantity: Int,
    @Json(name = "sold_quantity")
    val sold_quantity: Int,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "attributes")
    val attributes: List<AttributeResponse>,
    @Json(name = "pictures")
    val pictures: List<PictureResponse> = listOf()
)

@JsonClass(generateAdapter = true)
data class SellerResponse(
    @Json(name = "eshop")
    val eshop: EshopResponse?
)

@JsonClass(generateAdapter = true)
data class EshopResponse(
    @Json(name = "nick_name")
    val nick_name: String?
)

@JsonClass(generateAdapter = true)
data class PictureResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "secure_url")
    val secure_url: String
)

data class AttributeResponse(
    @Json(name = "name")
    val name: String,
    @Json(name = "value_name")
    val value_name: String?
)