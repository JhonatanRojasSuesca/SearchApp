package com.jhonatanrojas.searchapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.jhonatanrojas.searchapp.R
import com.jhonatanrojas.searchapp.databinding.FragmentDetailBinding
import com.jhonatanrojas.searchapp.ui.adapters.AttributesProductAdapter
import com.jhonatanrojas.searchapp.ui.adapters.ImagesDetailAdapter
import com.jhonatanrojas.searchapp.ui.bottomSheets.BottomSheetDialogGeneric
import com.jhonatanrojas.searchapp.ui.states.DetailState
import com.jhonatanrojas.searchapp.ui.viewModels.DetailProductViewModel
import com.jhonatanrojas.searchapp.utils.gone
import com.jhonatanrojas.searchapp.utils.pinchtozoom.ImageMatrixTouchHandler
import com.jhonatanrojas.searchapp.utils.setImageUrl
import com.jhonatanrojas.searchapp.utils.visible
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.abs

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailProductViewModel: DetailProductViewModel by viewModel()
    private val args: DetailFragmentArgs by navArgs()
    private val viewPagerAdapter by lazy {
        ImagesDetailAdapter(
            ::showImageZoom
        )
    }
    private val attributesAdapter by lazy {
        AttributesProductAdapter()
    }
    private val imageMatrixTouchHandler: ImageMatrixTouchHandler by lazy {
        ImageMatrixTouchHandler(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idProduct = args.idProduct
        setUpObserverLiveData()
        setupStateFlow()
        setUpAdapter()
        setUpListener()
        detailProductViewModel.getProductById(idProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpListener() {
        binding.logoMeli.imvArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.layoutInfoProduct.icArrowDown.setOnClickListener {
            binding.layoutInfoProduct.motionInfoProduct.transitionToStart()
        }
        binding.layoutInfoProduct.icArrowUp.setOnClickListener {
            binding.layoutInfoProduct.motionInfoProduct.transitionToEnd()
        }
        binding.cart.setOnClickListener {
            detailProductViewModel.addProductToCart()
            binding.cart.gone()
            binding.deleteCart.visible()
        }
        binding.deleteCart.setOnClickListener {
            detailProductViewModel.deleteProductCart()
            binding.cart.visible()
            binding.deleteCart.gone()
        }
        binding.imvCloseZoom.setOnClickListener { hideImageZoom() }
    }

    private fun setUpObserverLiveData() {
        detailProductViewModel.product.observe(viewLifecycleOwner, { product ->
            binding.apply {
                txvProductName.text = product.title
                txvPriceProduct.text = getString(R.string.price_product, product.price.toInt().toString())
                setPriceOrigin(product.originalPrice)
                setSoldQuantity(product.soldQuantity)
                viewPagerAdapter.setListImage(product.pictures)
                attributesAdapter.setListAttributes(product.attributes)
                if (product.isAddCart){
                    cart.gone()
                    deleteCart.visible()
                } else{
                    cart.visible()
                    deleteCart.gone()
                }
            }
        })
    }

    private fun setSoldQuantity(soldQuantity: Int) {
        binding.txvSoldQuantity.text = getString(
            R.string.sold_quantity,
            soldQuantity.toString()
        )
    }

    private fun setPriceOrigin(originalPrice: Double) {
        binding.txvPriceProductOrigin.apply {
            if (originalPrice != 0.0) {
                text = getString(R.string.price_origin, originalPrice.toInt().toString())
            } else {
                gone()
            }
        }
    }

    private fun setupStateFlow() {
        lifecycleScope.launchWhenCreated {
            detailProductViewModel.model.collect {
                detailProductUiStatusModel(it)
            }
        }
    }

    private fun detailProductUiStatusModel(searchStatus: DetailState) {
        when (searchStatus) {
            is DetailState.Loading -> {
                showLoadingFragment()
            }
            is DetailState.ShowErrorResource -> {
                showError(searchStatus.resource)
            }
            is DetailState.ShowHttpError -> {
                showHttpError(searchStatus.message)
            }
            else -> hideLoading()
        }
    }

    private fun setUpAdapter() {
        binding.viewpager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            adapter = viewPagerAdapter

            val compositePageTransformer = CompositePageTransformer()
            val pageTransformer = ViewPager2.PageTransformer { page, position ->
                var r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
            with(compositePageTransformer) {
                addTransformer(MarginPageTransformer(40))
                addTransformer(pageTransformer)
            }
            setPageTransformer(compositePageTransformer)
        }
        binding.layoutInfoProduct.rvAttributes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = attributesAdapter
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

    @SuppressLint("ClickableViewAccessibility")
    private fun showImageZoom(position: Int) {
        runCatching {
            detailProductViewModel.product.value?.apply {
                binding.imvZoomImage.setImageUrl(
                    this@DetailFragment.requireContext(),
                    url = getImageToViewPager(position)
                )

                val alphaAnimation = AlphaAnimation(FROM_ALPHA, TO_ALPHA).apply {
                    duration = DURATION_ANIMATION
                }
                binding.clContainerZoomImage.run {
                    startAnimation(alphaAnimation)
                    visible()
                }
                binding.imvZoomImage.setOnTouchListener(imageMatrixTouchHandler)
            }
        }
    }

    private fun getImageToViewPager(position: Int): String? =
        detailProductViewModel.product.value?.pictures?.get(position)?.url

    private fun hideImageZoom() {
        val alphaAnimation = AlphaAnimation(
            TO_ALPHA,
            FROM_ALPHA
        ).apply {
            duration = DURATION_ANIMATION
        }
        binding.clContainerZoomImage.run {
            startAnimation(alphaAnimation)
            gone()
        }
        imageMatrixTouchHandler.cancelAnimation()
    }

    private fun showLoadingFragment() {
        binding.loading.visible()
    }

    companion object {
        private const val DURATION_ANIMATION = 600L
        private const val FROM_ALPHA = 0.0F
        private const val TO_ALPHA = 1.0F
    }
}