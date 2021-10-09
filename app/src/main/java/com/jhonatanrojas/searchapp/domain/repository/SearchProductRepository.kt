package com.jhonatanrojas.searchapp.domain.repository

import com.jhonatanrojas.searchapp.domain.models.ProductsDomain
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
interface SearchProductRepository {
    fun searchProduct(search: String, offset: Int, limit: Int): Flow<ProductsDomain>
}