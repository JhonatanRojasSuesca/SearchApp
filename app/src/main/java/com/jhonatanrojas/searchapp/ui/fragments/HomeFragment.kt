package com.jhonatanrojas.searchapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jhonatanrojas.searchapp.databinding.FragmentHomeBinding
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.ui.adapters.SearchProductsAdapter
import com.jhonatanrojas.searchapp.ui.bottomSheets.BottomSheetDialogGeneric
import com.jhonatanrojas.searchapp.ui.states.SearchState
import com.jhonatanrojas.searchapp.ui.viewModels.SearchProductViewModel
import com.jhonatanrojas.searchapp.utils.Utils
import com.jhonatanrojas.searchapp.utils.Utils.isOnline
import com.jhonatanrojas.searchapp.utils.doOnEnd
import com.jhonatanrojas.searchapp.utils.gone
import com.jhonatanrojas.searchapp.utils.isNotEmptyEditText
import com.jhonatanrojas.searchapp.utils.visible
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
            ::goToDetail,
            ::clearOfSet,
            ::addCart,
            ::deleteProductCart
        )
    }

    private fun deleteProductCart(productResults: ProductResults) {
        searchProductViewModel.deleteProductCart(productResults)
    }

    var flag: Boolean = true

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                transitionMotion(firstVisibleItemPosition)

                if (isOnline(requireContext())) searchProductViewModel.onLoadMoreData(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
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
        managementOnBackHome()
        setUpObserverLiveData()
        setupStateFlow()
        setUpAdapter()
        setUpListeners()
    }

    override fun onResume() {
        super.onResume()
        searchProductViewModel.validateProductsInCart()
    }

    private fun managementOnBackHome() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.rcvProductsSearch.isVisible || binding.txvUps.isVisible) {
                    binding.rcvProductsSearch.gone()
                    binding.txvUps.gone()
                    binding.txvNotFound.gone()
                    binding.txvEmptyTittle.visible()
                    binding.emptySearch.visible()
                    binding.txvGeneral.visible()
                    binding.motionView.transitionToStart()
                } else {
                    requireActivity().finish()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpObserverLiveData() {
        searchProductViewModel.productsList.observe(viewLifecycleOwner, { products ->
            binding.let {
                if (products.isNullOrEmpty()) {
                    it.emptySearch.visible()
                    it.txvUps.visible()
                    it.txvNotFound.visible()
                    it.txvEmptyTittle.gone()
                    it.rcvProductsSearch.gone()
                    searchAdapter.clearAdapter()
                } else {
                    it.rcvProductsSearch.visible()
                    it.emptySearch.gone()
                    searchAdapter.setList(products, searchProductViewModel.textSearch)
                }
            }
        })
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
            it.btnToCart.setOnClickListener {
                Navigation.findNavController(requireView()).navigate(
                    HomeFragmentDirections.actionHomeToCart()
                )
            }
        }
    }

    private fun searchReference(searchProduct: String) {
        Utils.hideSoftKeyboard(requireActivity())
        searchProductViewModel.textSearch = searchProduct
        searchProductViewModel.searchProduct()
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
                showHttpError(searchStatus.message)
            }
            else -> hideLoading()
        }
    }

    fun transitionMotion(firstVisibleItemPosition: Int) {
        if (firstVisibleItemPosition > 0 && flag) {
            flag = false
            binding.motionView.transitionToEnd()
            binding.txvGeneral.gone()
        } else if (firstVisibleItemPosition == 0) {
            binding.txvGeneral.visible()
            binding.motionView.transitionToStart()
        }
        binding.motionView.doOnEnd {
            flag = true
        }
    }

    private fun hideLoading() {
        binding.loading.gone()
    }

    private fun showHttpError(message: String) {
        val bottomSheetDialogGeneric = BottomSheetDialogGeneric(
            description = message,
            cancelable = false
        )
        showBottomSheetDialog(bottomSheetDialogGeneric)
    }

    private fun showError(resource: Int) {
        val bottomSheetDialogGeneric = BottomSheetDialogGeneric(
            description = getString(resource),
            cancelable = false
        )
        showBottomSheetDialog(bottomSheetDialogGeneric)
    }

    private fun showBottomSheetDialog(dialogFragment: DialogFragment) {
        (this as? FragmentActivity)?.let {
            if (!dialogFragment.isAdded) {
                dialogFragment.show(it.supportFragmentManager, dialogFragment.tag)
            }
        }
    }

    private fun showLoadingFragment() {
        binding.loading.visible()
    }

    private fun setUpAdapter() {
        binding.rcvProductsSearch.apply {
            addOnScrollListener(onScrollListener)
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun goToDetail(product: ProductResults) {
       Navigation.findNavController(requireView()).navigate(
            HomeFragmentDirections.actionHomeToDetail(product.id)
       )
    }

    private fun addCart(productResults: ProductResults) {
        searchProductViewModel.addToCart(productResults)
    }

    private fun clearOfSet() {
        searchProductViewModel.offSet = 0
        binding.rcvProductsSearch.scrollToPosition(0)
    }
}