package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.animation.ValueAnimator

abstract class AbsCorrectorAnimatorHandler(private var corrector: ImageMatrixCorrector?) :
    ValueAnimator.AnimatorUpdateListener {

    private val floatArraySize = 9
    private var values: FloatArray = FloatArray(floatArraySize)

    protected open fun getCorrector(): ImageMatrixCorrector? {
        return corrector
    }

    protected open fun getValues(): FloatArray? {
        return values
    }
}
