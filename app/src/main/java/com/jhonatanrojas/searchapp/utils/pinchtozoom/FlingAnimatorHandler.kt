package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.animation.ValueAnimator
import android.graphics.Matrix

class FlingAnimatorHandler(corrector: ImageMatrixCorrector?) :
    AbsCorrectorAnimatorHandler(corrector) {

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val corrector = getCorrector()
        val imageView = corrector?.getImageView()
        val matrix = imageView?.matrix
        val values = getValues()
        matrix?.getValues(values)

        var dx =
            animation.getAnimatedValue(PROPERTY_TRANSLATE_X) as Float
        dx = corrector?.correctAbsolute(
            Matrix.MTRANS_X,
            dx
        )?.minus(values!![Matrix.MTRANS_X]) ?: 0f

        var dy =
            animation.getAnimatedValue(PROPERTY_TRANSLATE_Y) as Float
        dy = corrector?.correctAbsolute(
            Matrix.MTRANS_Y,
            dy
        )?.minus(values!![Matrix.MTRANS_Y]) ?: 0f

        matrix?.postTranslate(dx, dy)
        imageView?.invalidate()
    }

    companion object {
        const val PROPERTY_TRANSLATE_X = "translateX"
        const val PROPERTY_TRANSLATE_Y = "translateY"
    }
}
