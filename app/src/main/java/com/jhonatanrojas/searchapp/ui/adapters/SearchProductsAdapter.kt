package com.jhonatanrojas.searchapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jhonatanrojas.searchapp.databinding.ItemCardProductBinding
import com.jhonatanrojas.searchapp.domain.models.ProductResults
import com.jhonatanrojas.searchapp.utils.gone
import com.jhonatanrojas.searchapp.utils.setImageUrl
import com.jhonatanrojas.searchapp.utils.visible

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */
class SearchProductsAdapter(
    private val selectProduct: (ProductResults) -> Unit,
    private val clearOfSet: () -> Unit,
    private val addCart: (ProductResults) -> Unit
) : RecyclerView.Adapter<SearchProductsAdapter.SearchProductViewHolder>() {

    private var storageProduct: ArrayList<ProductResults> = arrayListOf()
    private var changeSearch: String = ""

    fun setList(listProducts: List<ProductResults>, newSearch: String) {
        if (changeSearch != newSearch) {
            changeSearch = newSearch
            clearAdapter()
        }
        storageProduct.addAll(listProducts)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        storageProduct = arrayListOf()
        storageProduct.clear()
        clearOfSet.invoke()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCardProductBinding.inflate(layoutInflater, parent, false)
        return SearchProductViewHolder(
            binding, addCart
        )
    }

    override fun getItemCount() = storageProduct.size

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        val product = storageProduct[position]
        holder.itemView.setOnClickListener { selectProduct(product) }
        holder.bind(product, holder.itemView.context)
    }

    open class SearchProductViewHolder(
        private var view: ItemCardProductBinding,
        val addCart: (ProductResults) -> Unit
    ) : RecyclerView.ViewHolder(view.root) {

        fun bind(product: ProductResults, context: Context) {
            view.apply {
                if(product.isAddCart){
                    cart.gone()
                }else{
                    cart.visible()
                }
                cart.setOnClickListener { addCart(product) }
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
