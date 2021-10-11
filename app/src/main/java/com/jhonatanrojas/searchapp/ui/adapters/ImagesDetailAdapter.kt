package com.jhonatanrojas.searchapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.jhonatanrojas.searchapp.databinding.ItemImageProductBinding
import com.jhonatanrojas.searchapp.domain.models.Picture
import com.jhonatanrojas.searchapp.utils.setImageUrl

/**
 * Created by JHONATAN ROJAS on 10/10/2021.
 */
class ImagesDetailAdapter : RecyclerView.Adapter<ImagesDetailAdapter.ImageViewHolder>() {

    private var imagesProducts: ArrayList<Picture> = arrayListOf()

    fun setListImage(picturesList: List<Picture>) {
        imagesProducts = arrayListOf()
        imagesProducts.clear()
        imagesProducts.addAll(picturesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemImageProductBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val product = imagesProducts[position]
        holder.bind(product, holder.itemView.context)
    }

    override fun getItemCount() = imagesProducts.size

    open class ImageViewHolder(private var view: ItemImageProductBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(picture: Picture, context: Context) {
            view.apply {
                imvImageProduct.setImageUrl(context, picture.url)
            }
        }
    }
}

class ImagesDetailAdapter2 : PagerAdapter() {

    private var imagesProducts: ArrayList<Picture> = arrayListOf()

    fun setListImage(picturesList: List<Picture>) {
        imagesProducts = arrayListOf()
        imagesProducts.clear()
        imagesProducts.addAll(picturesList)
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val binding = ItemImageProductBinding.inflate(layoutInflater, container, false)
        val product = imagesProducts[position]
        binding.imvImageProduct.setImageUrl(container.context, product.url)
        return binding.root
    }

    override fun getCount() = imagesProducts.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}