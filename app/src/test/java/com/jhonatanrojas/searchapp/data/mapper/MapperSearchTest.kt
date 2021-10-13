package com.jhonatanrojas.searchapp.data.mapper

import com.jhonatanrojas.searchapp.BaseTest
import com.jhonatanrojas.searchapp.data.mappers.mapToDomainProduct
import com.jhonatanrojas.searchapp.data.mappers.mapToDomainSearchProducts
import com.jhonatanrojas.searchapp.data.response.AttributeResponse
import com.jhonatanrojas.searchapp.data.response.EshopResponse
import com.jhonatanrojas.searchapp.data.response.PictureResponse
import com.jhonatanrojas.searchapp.data.response.ProductResponse
import com.jhonatanrojas.searchapp.data.response.SearchProductsResponse
import com.jhonatanrojas.searchapp.data.response.SellerResponse
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

/**
 * Created by JHONATAN ROJAS on 12/10/2021.
 */
class MapperSearchTest : BaseTest() {

    @Test
    fun mapToProductsDomainModel() {
        val searchProductsResponse: SearchProductsResponse = mock()
        val productResponse: ProductResponse = mock()
        val sellerResponse: SellerResponse = mock()
        val eshopResponse: EshopResponse = mock()
        val listProductResponse: ArrayList<ProductResponse> = arrayListOf()

        listProductResponse.add(productResponse)

        `when`(searchProductsResponse.results).thenReturn(listProductResponse)
        `when`(productResponse.id).thenReturn("23")
        `when`(productResponse.title).thenReturn("PRODUCT")
        `when`(productResponse.price).thenReturn(2.5)
        `when`(productResponse.seller).thenReturn(sellerResponse)
        `when`(sellerResponse.eshop).thenReturn(eshopResponse)
        `when`(eshopResponse.nick_name).thenReturn("alkosto")
        `when`(productResponse.thumbnail).thenReturn("www.t.com")

        val result = mapToDomainSearchProducts(searchProductsResponse)

        assertEquals(result.productsSearch.first().id, "23")
        assertEquals(result.productsSearch.first().title, "PRODUCT")
        assertEquals(result.productsSearch.first().price, 2.5)
        assertEquals(result.productsSearch.first().seller?.nick_name, "alkosto")
        assertEquals(result.productsSearch.first().thumbnail, "www.t.com")

        verify(searchProductsResponse).results
        verify(productResponse).id
        verify(productResponse).title
        verify(productResponse).price
        verify(productResponse, times(2)).seller
        verify(sellerResponse).eshop
        verify(eshopResponse).nick_name
        verify(productResponse).thumbnail

        verifyNoMoreInteractions(searchProductsResponse, productResponse, sellerResponse, eshopResponse)
    }

    @Test
    fun mapToDomainProductModel() {
        val productResponse: ProductResponse = mock()
        val sellerResponse: SellerResponse = mock()
        val eshopResponse: EshopResponse = mock()
        val pictureResponse: PictureResponse = mock()
        val attributeResponse: AttributeResponse = mock()

        val listPictures: ArrayList<PictureResponse> = arrayListOf()
        val listAttribute: ArrayList<AttributeResponse> = arrayListOf()
        listPictures.add(pictureResponse)
        listAttribute.add(attributeResponse)

        `when`(productResponse.id).thenReturn("23")
        `when`(productResponse.title).thenReturn("PRODUCT")
        `when`(productResponse.price).thenReturn(2.5)
        `when`(productResponse.original_price).thenReturn(2.6)
        `when`(productResponse.seller).thenReturn(sellerResponse)
        `when`(sellerResponse.eshop).thenReturn(eshopResponse)
        `when`(eshopResponse.nick_name).thenReturn("alkosto")
        `when`(productResponse.available_quantity).thenReturn(123)
        `when`(productResponse.sold_quantity).thenReturn(10)
        `when`(productResponse.thumbnail).thenReturn("www.t.com")
        `when`(productResponse.pictures).thenReturn(listPictures)
        `when`(pictureResponse.id).thenReturn("123")
        `when`(pictureResponse.secure_url).thenReturn("www.sq.com")
        `when`(pictureResponse.url).thenReturn("www.url.com")
        `when`(productResponse.attributes).thenReturn(listAttribute)
        `when`(attributeResponse.name).thenReturn("name attribute")
        `when`(attributeResponse.value_name).thenReturn("no")

        val result = mapToDomainProduct(productResponse)

        assertEquals(result.id, "23")
        assertEquals(result.title, "PRODUCT")
        assertEquals(result.price, 2.5)
        assertEquals(result.originalPrice, 2.6)
        assertEquals(result.seller?.nick_name, "alkosto")
        assertEquals(result.availableQuantity, 123)
        assertEquals(result.soldQuantity, 10)
        assertEquals(result.thumbnail, "www.t.com")
        assertEquals(result.pictures.first().id, "123")
        assertEquals(result.pictures.first().secure_url, "www.sq.com")
        assertEquals(result.pictures.first().url, "www.url.com")
        assertEquals(result.attributes.first().name, "name attribute")
        assertEquals(result.attributes.first().valueName, "no")


        verify(productResponse).id
        verify(productResponse).title
        verify(productResponse).price
        verify(productResponse).original_price
        verify(productResponse, times(2)).seller
        verify(sellerResponse).eshop
        verify(eshopResponse).nick_name
        verify(productResponse).available_quantity
        verify(productResponse).sold_quantity
        verify(productResponse).thumbnail
        verify(productResponse).pictures
        verify(pictureResponse).id
        verify(pictureResponse).secure_url
        verify(pictureResponse).url
        verify(productResponse).attributes
        verify(attributeResponse).name
        verify(attributeResponse).value_name

        verifyNoMoreInteractions(
            productResponse,
            sellerResponse,
            eshopResponse,
            pictureResponse,
            attributeResponse
        )
    }
}