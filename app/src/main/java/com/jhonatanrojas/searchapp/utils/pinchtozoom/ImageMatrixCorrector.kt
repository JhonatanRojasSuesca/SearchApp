package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.graphics.Matrix
import android.widget.ImageView

abstract class ImageMatrixCorrector : MatrixCorrector() {

    private var imageView: ImageView? = null

    /**
     *
     * Returns the width of the scaled image.
     * @return
     */
    private var scaledImageWidth = 0f

    /**
     * *
     *
     *Returns the height of the scaled image.
     * @return
     */
    private var scaledImageHeight = 0f

    override fun setMatrix(matrix: Matrix?) {
        super.setMatrix(matrix)
        updateScaledImageDimensions()
    }

    /**
     *
     * Sets the `ImageView`.
     * This also sets its inner image matrix as this corrector's matrix automatically.
     * @param imageView
     */
    fun setImageView(imageView: ImageView?) {
        // TODO Make a weak reference or set to null in order to avoid memory leaks
        this.imageView = imageView
        if (imageView != null) {
            setMatrix(imageView.imageMatrix)
        }
    }

    /**
     *
     * @return
     */
    fun getImageView(): ImageView? {
        return imageView
    }

    /**
     *
     * @return
     */
    val innerFitScale: Float
        get() {
            val drawable = imageView!!.drawable
            val widthRatio =
                drawable.intrinsicWidth.toFloat() / imageView!!.width
            val heightRatio =
                drawable.intrinsicHeight.toFloat() / imageView!!.height
            return if (widthRatio > heightRatio) {
                1f / widthRatio
            } else {
                1f / heightRatio
            }
        }

    /**
     *
     * (Re)calculates the image's current dimensions.
     */
    fun updateScaledImageDimensions() {
        val values = getValues()
        val drawable = imageView?.drawable
        if (drawable != null) {
            scaledImageWidth =
                values[Matrix.MSCALE_X] * drawable.intrinsicWidth
            scaledImageHeight =
                values[Matrix.MSCALE_Y] * drawable.intrinsicHeight
        } else {
            scaledImageHeight = 0f
            scaledImageWidth = scaledImageHeight
        }
    }

    /**
     *
     * Returns the width of the scaled image.
     * @return
     */
    protected open fun getScaledImageWidth(): Float {
        return scaledImageWidth
    }

    /**
     * *
     *
     *Returns the height of the scaled image.
     * @return
     */
    protected open fun getScaledImageHeight(): Float {
        return scaledImageHeight
    }
}
