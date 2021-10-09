package com.jhonatanrojas.searchapp.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jhonatanrojas.searchapp.R

/**
 * Created by JHONATAN ROJAS on 9/10/2021.
 */

fun ImageView.setImageUrl(context: Context, url: String?) {
    Glide.with(this.context)
        .load(url ?: "")
        .placeholder(R.drawable.delivery_box)
        .into(this)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun EditText.isNotEmptyEditText(): Boolean {
    return this.text.toString().trim().isNotEmpty()
}