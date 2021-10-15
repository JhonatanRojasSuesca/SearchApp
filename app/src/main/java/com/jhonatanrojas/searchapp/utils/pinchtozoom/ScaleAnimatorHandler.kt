package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.animation.ValueAnimator
import android.graphics.Matrix
import com.jhonatanrojas.searchapp.utils.pinchtozoom.AbsCorrectorAnimatorHandler
import com.jhonatanrojas.searchapp.utils.pinchtozoom.ImageMatrixCorrector

class ScaleAnimatorHandler(
    corrector: ImageMatrixCorrector?,
    private var px: Float,
    private var py: Float
) : AbsCorrectorAnimatorHandler(corrector) {

    private var translate: Boolean = true

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val corrector = getCorrector()
        val imageView = corrector?.getImageView()
        if (imageView?.drawable != null) {
            val matrix = imageView.imageMatrix
            val values = getValues()
            matrix.getValues(values)
            var sx = animation.animatedValue as Float
            sx = corrector.correctAbsolute(
                Matrix.MSCALE_X,
                sx
            ) / values!![Matrix.MSCALE_X]
            if (translate) {
                matrix.postScale(sx, sx, px, py)
            } else {
                matrix.postScale(sx, sx)
            }
            corrector.performAbsoluteCorrections()
            imageView.invalidate()
        }
    }
}
