package com.jhonatanrojas.searchapp.data.response



/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
data class SearchProductsResponse(
    val site_id: String,
    val paging: PagingResponse,
    val results: List<ProductResponse>
)
data class PagingResponse(val total: Int,val offset:Int,val limit: Int)

data class ProductResponse(
    val id: String,
    val title: String,
    val price: Double,
    val original_price: Double?,
    val seller: SellerResponse?,
    val available_quantity: Int,
    val sold_quantity: Int,
    val thumbnail: String,
    val pictures: List<PictureResponse>,
    val attributes: List<AttributeResponse>
)

data class SellerResponse(val eshop: EshopResponse?)

data class EshopResponse(val nick_name: String?)

data class PictureResponse(val id:String, val url: String, val secure_url: String)

data class AttributeResponse(val name: String, val value_name: String)