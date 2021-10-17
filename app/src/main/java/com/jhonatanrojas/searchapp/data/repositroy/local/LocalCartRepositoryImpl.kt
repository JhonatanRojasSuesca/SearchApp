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

    /**
     *trae la lista de productos agregados a la base de datos de la tabla cartTable
     */
    override fun getProductsCart(): Flow<List<ProductResults>> {
        return cartDao.getCart().map { mapperToDomainCart(it) }
    }

    /**
     *trae la lista de strings de ids agregados a la base de datos de la tabla cartTable para validacion
     */
    override suspend fun getListIdsCart(): List<String> {
        return cartDao.getIdsCart()
    }

    /**
     *inserta el producto a la base datos en la tabla cartTable y antes se mappea de productResults a CartProductModel
     */
    override suspend fun insertProduct(productResults: ProductResults) {
        cartDao.addCart(mapperToCartDomain(productResults))
    }

    /**
     * trae el producto por el id de la tabla cartTable para validar si exite en la base de datos  y se mapea a una lista de ProductsResults
     */
    override suspend fun productIsAddCart(id: String): List<ProductResults> {
        return mapperToDomainCart(cartDao.getProductCart(id))
    }

    /**
     * elimina el producto de la base de datos  y mappea de productResults a CartProductModel
     */
    override suspend fun deleteItem(productResults: ProductResults): Int {
        return cartDao.deleteItemCard(mapperToCartDomain(productResults))
    }

    /**
     * elimina todos los items de la tabla cartTable
     */
    override suspend fun deleteAllCart() {
        cartDao.deleteAllChannels()
    }
}