package com.jhonatanrojas.searchapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jhonatanrojas.searchapp.databinding.ItemAttributeBinding
import com.jhonatanrojas.searchapp.domain.models.Attribute

/**
 * Created by JHONATAN ROJAS on 11/10/2021.
 */
class AttributesProductAdapter : RecyclerView.Adapter<AttributesProductAdapter.AttributeViewHolder>() {

    private var attributesProducts: ArrayList<Attribute> = arrayListOf()

    fun setListAttributes(attributesList: List<Attribute>) {
        attributesProducts = arrayListOf()
        attributesProducts.clear()
        attributesProducts.addAll(attributesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAttributeBinding.inflate(layoutInflater, parent, false)
        return AttributeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        val product = attributesProducts[position]
        holder.bind(product, holder.itemView.context)
    }

    override fun getItemCount() = attributesProducts.size

    open class AttributeViewHolder(private var view: ItemAttributeBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(attribute: Attribute, context: Context) {
            view.apply {
                txvAttributeName.text = attribute.name
                txvAttributeValue.text = attribute.valueName
            }
        }
    }
}