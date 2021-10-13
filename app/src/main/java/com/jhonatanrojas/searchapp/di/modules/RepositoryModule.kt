package com.jhonatanrojas.searchapp.di.modules

import com.jhonatanrojas.searchapp.data.mappers.mapToDomainProduct
import com.jhonatanrojas.searchapp.data.mappers.mapToDomainSearchProducts
import com.jhonatanrojas.searchapp.data.repositroy.DetailProductByIdRepositoryImpl
import com.jhonatanrojas.searchapp.data.repositroy.SearchProductRepositoryImpl
import com.jhonatanrojas.searchapp.data.repositroy.exceptionImpl.ExceptionSearchProductImpl
import com.jhonatanrojas.searchapp.domain.repositoryInterface.DetailProductByIdRepository
import com.jhonatanrojas.searchapp.domain.repositoryInterface.SearchProductRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

val repositoryModule: Module = module {

    factory<SearchProductRepository> {
        SearchProductRepositoryImpl(
            searchProductApi = get(),
            exceptionSearchProductImpl = ExceptionSearchProductImpl(),
            mapSearchProduct = mapToDomainSearchProducts
        )
    }

    factory<DetailProductByIdRepository> {
        DetailProductByIdRepositoryImpl(
            searchProductApi = get(),
            exceptionSearchProductImpl = ExceptionSearchProductImpl(),
            mapDomainProduct = mapToDomainProduct
        )
    }
}

