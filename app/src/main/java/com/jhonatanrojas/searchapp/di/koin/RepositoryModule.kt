package com.jhonatanrojas.searchapp.di.koin

import com.jhonatanrojas.searchapp.data.mappers.mapToDomainSearchProducts
import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.data.repositroy.SearchProductRepositoryImpl
import com.jhonatanrojas.searchapp.data.repositroy.exceptionImpl.ExceptionSearchProductImpl
import com.jhonatanrojas.searchapp.di.network.NetworkClient
import com.jhonatanrojas.searchapp.domain.repository.SearchProductRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

val repositoryModule: Module = module {

    factory<SearchProductRepository> {
        SearchProductRepositoryImpl(
            searchProductApi = get(),
            exceptionSupplyManualSearchProductImpl = ExceptionSearchProductImpl(),
            mapSearchProduct = mapToDomainSearchProducts
        )
    }
}

