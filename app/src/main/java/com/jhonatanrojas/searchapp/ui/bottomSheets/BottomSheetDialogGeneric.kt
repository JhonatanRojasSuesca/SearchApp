package com.jhonatanrojas.searchapp.ui.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jhonatanrojas.searchapp.databinding.BottomSheetErrorProductsReturnedBinding

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
class BottomSheetDialogGeneric(
    private val description: String,
    private val cancelable: Boolean,
    private val onPositiveAction: () -> Unit = {}
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetErrorProductsReturnedBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetErrorProductsReturnedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = cancelable
        initUI()
        initButtonsListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        with(binding) {
            txvDescriptionErrorProducts.text = description
        }
    }

    private fun initButtonsListeners() {
        binding.apply {
            btnRetry.setOnClickListener {
                onPositiveAction.invoke()
                dismiss()
            }
        }
    }
}