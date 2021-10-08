package com.jhonatanrojas.searchapp.di.koin

import com.jhonatanrojas.searchapp.data.networkApi.SearchProductApi
import com.jhonatanrojas.searchapp.di.network.NetworkClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

val retrofitModule: Module = module {

    single(named("retrofitApi")) {
        NetworkClient().getRetrofitInstance("https://api.mercadolibre.com/")
    }

    single { get<Retrofit>(named("retrofitApi")).create(SearchProductApi::class.java) }
}

