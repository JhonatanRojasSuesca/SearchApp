package com.jhonatanrojas.searchapp.data.networkApi

import retrofit2.http.GET

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
interface SearchProductApi {

    @GET("/sites/MLA/search?")
    fun searchProductQuery()
}