package com.jhonatanrojas.searchapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.databinding.FragmentCartBinding
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.ui.adapters.CartProductsAdapter
import com.jhonatanrojas.searchapp.ui.states.CartState
import com.jhonatanrojas.searchapp.ui.viewModels.CartProductViewModel
import com.jhonatanrojas.searchapp.utils.gone
import com.jhonatanrojas.searchapp.utils.visible
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class CartFragment: Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartProductViewModel: CartProductViewModel by inject()

    private val cartAdapter by lazy {
        CartProductsAdapter(
            ::goToDetailProduct,
            ::deleteItemCart,
            )
    }

    private fun goToDetailProduct(productResults: ProductResults) {
        Navigation.findNavController(requireView()).navigate(
            CartFragmentDirections.actionCartFragmentToDetailFragment(productResults.id)
        )
    }

    private fun deleteItemCart(productResults: ProductResults) {
        cartProductViewModel.deleteProductCart(productResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHeader()
        setUpAdapter()
        setupStateFlow()
        setUpListener()
        setUpObserverLiveData()
        cartProductViewModel.getProductsCart()
    }

    private fun setUpAdapter() {
        binding.recyclerCartProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setUpObserverLiveData() {
        cartProductViewModel.productsList.observe(viewLifecycleOwner, { products ->
            binding.let {
                if (products.isNullOrEmpty()) {
                    cartAdapter.clearAdapter()
                    it.messageEmpty.visible()
                } else {
                    it.messageEmpty.gone()
                    cartAdapter.setList(products)
                }
                hideLoading()
            }
        })
    }

    private fun setUpListener() {
        binding.logoMeli.imvArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.buttonEmptyCart.setOnClickListener {
            cartProductViewModel.deleteAllCart()
        }
    }

    private fun setupStateFlow() {
        lifecycleScope.launchWhenCreated {
            cartProductViewModel.model.collect {
                searchProductUiStatusModel(it)
            }
        }
    }

    private fun searchProductUiStatusModel(cartState: CartState) {
        when (cartState) {
            is CartState.Loading -> {
                showLoadingFragment()
            }
            else -> hideLoading()
        }
    }

    private fun hideLoading() {
        binding.loading.gone()
    }

    private fun showLoadingFragment() {
        binding.loading.visible()
    }

    private fun setUpHeader() {
        binding.logoMeli.txvGeneralScan.text = getString(R.string.cartTitle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}