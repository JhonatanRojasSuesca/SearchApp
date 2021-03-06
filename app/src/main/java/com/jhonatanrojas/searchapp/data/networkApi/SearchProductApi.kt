package com.jhonatanrojas.searchapp.data.networkApi

import com.jhonatanrojas.searchapp.data.response.ProductResponse
import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
interface SearchProductApi {

    @GET("/sites/MLA/search")
    suspend fun searchProducts(
        @Query("q") search: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): SearchProductsResponse

    @GET("/items/{ids}")
    suspend fun getProductDetail(
        @Path("ids") id: String
    ): ProductResponse
}