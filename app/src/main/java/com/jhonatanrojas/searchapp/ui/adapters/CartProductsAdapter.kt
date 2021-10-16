package com.jhonatanrojas.searchapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jhonatanrojas.searchapp.databinding.ItemCartProductBinding
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.utils.gone
import com.jhonatanrojas.searchapp.utils.setImageUrl
import com.jhonatanrojas.searchapp.utils.visible

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
class CartProductsAdapter(
    private val deleteProduct: (ProductResults) -> Unit
) : RecyclerView.Adapter<CartProductsAdapter.SearchProductViewHolder>() {

    private var storageProduct: ArrayList<ProductResults> = arrayListOf()

    fun setList(listProducts: List<ProductResults>) {
        clearAdapter()
        storageProduct.addAll(listProducts)
        notifyDataSetChanged()
    }
    fun clearAdapter(){
        storageProduct = arrayListOf()
        storageProduct.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCartProductBinding.inflate(layoutInflater, parent, false)
        return SearchProductViewHolder(
            binding, deleteProduct
        )
    }

    override fun getItemCount() = storageProduct.size

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        val product = storageProduct[position]
        holder.bind(product, holder.itemView.context)
    }

    open class SearchProductViewHolder(
        private var view: ItemCartProductBinding,
        val deleteProduct: (ProductResults) -> Unit
    ) : RecyclerView.ViewHolder(view.root) {

        fun bind(product: ProductResults, context: Context) {
            view.apply {
                deleteCart.setOnClickListener {
                    deleteProduct(product)
                }
                txvProductName.text = product.title
                txvProductPrice.text = "$  ${product.price.toInt()}"
                imvProduct.setImageUrl(context, product.thumbnail)
                product.seller?.apply {
                    if (nick_name.isNullOrEmpty()) {
                        txvNameSeller.gone()
                        txvTitleSellerBy.gone()
                    } else {
                        txvNameSeller.text = nick_name
                        txvNameSeller.visible()
                        txvTitleSellerBy.visible()
                    }
                }
                lnlItemProduct.elevation = ELEVATION_CARD
            }
        }
    }

    companion object {
        private var ELEVATION_CARD = 4F
    }
}
