package com.jhonatanrojas.searchapp.di.modules

import com.jhonatanrojas.searchapp.domain.useCase.GetProductDetailUC
import com.jhonatanrojas.searchapp.domain.useCase.SearchProductUC
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

val useCaseModule: Module = module {
    factory {
        SearchProductUC(
            searchProductRepository = get()
        )
    }
    factory {
        GetProductDetailUC(
            detailProductByIdRepository = get()
        )
    }
}
