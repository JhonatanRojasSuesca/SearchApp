package com.jhonatanrojas.searchapp.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
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

fun MotionLayout.doOnEnd(onEnd: () -> Unit) {
    this.setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onEnd()
        }
    })
}