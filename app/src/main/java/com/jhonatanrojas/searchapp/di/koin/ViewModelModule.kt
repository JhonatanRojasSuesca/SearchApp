package com.jhonatanrojas.searchapp.di.koin

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
}

