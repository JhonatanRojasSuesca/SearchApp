package com.jhonatanrojas.searchapp.data.mappers

import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import com.jhonatanrojas.searchapp.utils.Mapper

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
val mapToDomainSearchProducts: Mapper<
    SearchProductsResponse, ProductsDomain> =
    { input ->
        with(input) {
            ProductsDomain(
                productsSearch = listOf()
            )
        }
    }