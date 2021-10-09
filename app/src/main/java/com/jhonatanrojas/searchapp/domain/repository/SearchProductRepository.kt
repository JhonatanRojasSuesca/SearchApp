package com.jhonatanrojas.searchapp.domain.repository

import com.jhonatanrojas.searchapp.domain.models.Product
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
interface SearchProductRepository {
    fun searchProduct(reference: String) : Flow<List<Product>>
}