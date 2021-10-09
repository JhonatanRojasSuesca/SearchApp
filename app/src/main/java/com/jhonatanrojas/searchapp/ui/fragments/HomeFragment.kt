package com.jhonatanrojas.searchapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jhonatanrojas.searchapp.databinding.FragmentHomeBinding
import com.jhonatanrojas.searchapp.domain.models.Product
import com.jhonatanrojas.searchapp.ui.adapters.SearchProductsAdapter
import com.jhonatanrojas.searchapp.ui.states.SearchState
import com.jhonatanrojas.searchapp.ui.viewModels.SearchProductViewModel
import com.jhonatanrojas.searchapp.utils.Utils
import com.jhonatanrojas.searchapp.utils.isNotEmptyEditText
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val searchProductViewModel: SearchProductViewModel by viewModel()

    private val searchAdapter by lazy {
        SearchProductsAdapter(
            ::goToDetail
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStateFlow()
        setUpAdapter()
        setUpListeners()
    }

    private fun setupStateFlow() {
        lifecycleScope.launchWhenCreated {
            searchProductViewModel.model.collect {
                searchProductUiStatusModel(it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        binding.let {
            it.edtReferenceProduct.setOnEditorActionListener { editText, actionId, _ ->
                if ((actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.ACTION_DOWN)
                    && it.edtReferenceProduct.isNotEmptyEditText()
                ) {
                    searchReference(editText.text.toString())
                }
                return@setOnEditorActionListener true
            }

            it.edtReferenceProduct.setOnTouchListener(
                View.OnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= it.edtReferenceProduct.right -
                            it.edtReferenceProduct.compoundDrawables[2].bounds.width()
                        ) {
                            searchReference(it.edtReferenceProduct.text.toString())
                            return@OnTouchListener true
                        }
                    }
                    false
                }
            )
        }
    }

    private fun searchReference(searchProduct: String) {
        Utils.hideSoftKeyboard(requireActivity())
        searchProductViewModel.searchProduct(searchProduct)
    }

    private fun searchProductUiStatusModel(searchStatus: SearchState) {
        binding.edtReferenceProduct.text?.clear()
        when (searchStatus) {
            is SearchState.Loading -> {
                showLoadingFragment()
            }
            is SearchState.ShowErrorResource -> {
                showError(searchStatus.resource)
            }
            is SearchState.ShowHttpError -> {
                showHttpError(searchStatus.code, searchStatus.message)
            }
            is SearchState.Success -> {
                binding.let {
                    val listProducts = searchStatus.searchProducts.productsSearch
                    if (listProducts.isNullOrEmpty()) {
                        //  it.lnyReferenceWarning.visible()
                        // it.rcvListSupplyTasks.gone()
                    } else {
                        //it.lnyReferenceWarning.gone()
                        //it.rcvListSupplyTasks.visible()
                        searchAdapter.setList(listProducts)
                    }
                }
            }
            else -> hideLoading()
        }
    }

    private fun hideLoading() {
    }

    private fun showHttpError(code: Int, message: String) {
    }

    private fun showError(resource: Int) {
    }

    private fun showLoadingFragment() {
    }

    private fun setUpAdapter() {
        binding.rcvProductsSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun goToDetail(product: Product) {
    }
}