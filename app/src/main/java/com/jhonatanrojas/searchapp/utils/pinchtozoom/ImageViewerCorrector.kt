package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.graphics.Matrix
import android.widget.ImageView

/**
 * <p>This <code>MatrixCorrector</code> implementation defines the default behavior for an image viewer.</p>
 * <p>It works properly only if the following two conditions are met:</p>
 * <ol>
 * <li>There are no rotations</li>
 * <li>The scaling is uniform: <code>sx</code> and <code>sy</code> are always the same value</li>
 * </ol>
 */
class ImageViewerCorrector : ImageMatrixCorrector() {

    private var maxScale = 0f
    private var maxScaleRelative = false
    private val maxScaleView = 4f

    init {
        this(null, maxScaleView)
    }

    private operator fun invoke(
        imageView: ImageView?,
        maxScaleView: Float
    ) {
        imageView?.let { setImageView(it) }
        maxScale = maxScaleView
    }

    override fun performAbsoluteCorrections() {
        super.performAbsoluteCorrections()

        // Calculate the image's new dimensions
        updateScaledImageDimensions()

        // Correct the translations
        val values = getValues()
        values[Matrix.MTRANS_X] = correctAbsolute(
            Matrix.MTRANS_X,
            values[Matrix.MTRANS_X]
        )
        values[Matrix.MTRANS_Y] = correctAbsolute(
            Matrix.MTRANS_Y,
            values[Matrix.MTRANS_Y]
        )

        // Update the matrix
        getMatrix()?.setValues(values)
    }

    override fun correctAbsolute(vector: Int, x: Float): Float {
        return when (vector) {
            Matrix.MTRANS_X -> correctTranslation(
                x,
                getImageView()!!.width.toFloat(),
                getScaledImageWidth()
            )
            Matrix.MTRANS_Y -> correctTranslation(
                x,
                getImageView()!!.height.toFloat(),
                getScaledImageHeight()
            )
            Matrix.MSCALE_X, Matrix.MSCALE_Y -> {
                val innerFitScale: Float = innerFitScale
                val maxScale =
                    if (maxScaleRelative) innerFitScale * maxScale else maxScale
                x.coerceAtMost(maxScale).coerceAtLeast(innerFitScale)
            }
            else -> throw IllegalArgumentException("Vector not supported")
        }
    }

    /**
     *
     * Corrects the translation so that it does not exceed the allowed bounds.
     * @param translations
     * @param viewDim
     * @param imgDim
     * @return
     */
    private fun correctTranslation(
        translations: Float,
        viewDim: Float,
        imgDim: Float
    ): Float {
        var translation = translations
        translation = if (imgDim < viewDim) {
            // Must center
            viewDim / 2 - imgDim / 2
        } else {
            val diff = imgDim - viewDim
            0f.coerceAtMost(translation).coerceAtLeast(-diff)
        }
        return translation
    }
}
