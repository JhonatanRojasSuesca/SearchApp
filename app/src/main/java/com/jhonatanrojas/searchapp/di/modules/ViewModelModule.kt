package com.jhonatanrojas.searchapp.di.modules

import com.jhonatanrojas.searchapp.ui.viewModels.CartProductViewModel
import com.jhonatanrojas.searchapp.ui.viewModels.DetailProductViewModel
import com.jhonatanrojas.searchapp.ui.viewModels.SearchProductViewModel
import com.jhonatanrojas.searchapp.utils.manageErrorsToPresentation
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */

val viewModelModule: Module = module {

    viewModel {
        SearchProductViewModel(
            searchProductUC = get(),
            mapperExceptions = manageErrorsToPresentation()
        )
    }

    viewModel {
        DetailProductViewModel(
            getProductDetailUC = get(),
            mapperExceptions = manageErrorsToPresentation()
        )
    }

    viewModel {
        CartProductViewModel(
            managementProductLocalCartUC = get()
        )
    }
}

