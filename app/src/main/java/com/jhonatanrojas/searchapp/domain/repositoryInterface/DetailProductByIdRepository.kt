package com.jhonatanrojas.searchapp.domain.repositoryInterface

import com.jhonatanrojas.searchapp.domain.models.Product
import kotlinx.coroutines.flow.Flow

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
interface DetailProductByIdRepository {
    fun getProductById(id: String): Flow<Product>
}