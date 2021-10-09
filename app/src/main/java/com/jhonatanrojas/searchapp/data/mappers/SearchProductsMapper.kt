package com.jhonatanrojas.searchapp.data.mappers

import com.jhonatanrojas.searchapp.data.response.AttributeResponse
import com.jhonatanrojas.searchapp.data.response.PictureResponse
import com.jhonatanrojas.searchapp.data.response.ProductResponse
import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import com.jhonatanrojas.searchapp.data.response.SellerResponse
import com.jhonatanrojas.searchapp.domain.models.Attribute
import com.jhonatanrojas.searchapp.domain.models.Eshop
import com.jhonatanrojas.searchapp.domain.models.Picture
import com.jhonatanrojas.searchapp.domain.models.Product
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
                productsSearch = results.map(mapToDomainProduct)
            )
        }
    }

val mapToDomainProduct: Mapper<ProductResponse, Product> =
    { input ->
        with(input) {
            Product(
                id = id,
                title = title,
                price = price,
                original_price = original_price ?: 0.0,
                eshop = seller?.let { mapToDomainEshop(seller) },
                available_quantity = available_quantity,
                sold_quantity = sold_quantity,
                thumbnail = thumbnail,
                pictures = pictures.map(mapToDomainPicture),
                attributes = attributes.map(mapToDomainAttributes)
            )
        }
    }

val mapToDomainEshop: Mapper<SellerResponse, Eshop> =
    { input ->
        with(input) {
            Eshop(
                nick_name = eshop?.nick_name ?: ""
            )
        }
    }

val mapToDomainPicture: Mapper<PictureResponse, Picture> =
    { input ->
        with(input) {
            Picture(
                id = id,
                url = url,
                secure_url = secure_url
            )
        }
    }

val mapToDomainAttributes: Mapper<AttributeResponse, Attribute> =
    { input ->
        with(input) {
            Attribute(
                name = name,
                value_name = value_name
            )
        }
    }
