package com.jhonatanrojas.searchapp.data.repositroy.local

import com.jhonatanrojas.searchapp.data.modelLocal.CartDao
import com.jhonatanrojas.searchapp.data.modelLocal.CartProductModel
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.domain.repositoryInterface.LocalCartRepository
import com.jhonatanrojas.searchapp.utils.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
class LocalCartRepositoryImpl(
    private val cartDao: CartDao,
    private val mapperToDomainCart: Mapper<List<CartProductModel>, List<ProductResults>>,
    private val mapperToCartDomain: Mapper<ProductResults, CartProductModel>
) : LocalCartRepository {

    override fun getProductsCart(): Flow<List<ProductResults>> {
        return cartDao.getCart().map { mapperToDomainCart(it) }
    }

    override fun insertProduct(productResults: ProductResults) {
        cartDao.addCart(mapperToCartDomain(productResults))
    }

    override fun deleteItem(productResults: ProductResults): Int {
        return cartDao.deleteItemCard(mapperToCartDomain(productResults))
    }
}