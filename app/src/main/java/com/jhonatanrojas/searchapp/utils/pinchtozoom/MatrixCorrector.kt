package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.graphics.Matrix

/**
 * <p>The <code>MatrixCorrector</code> enforces boundaries in the transformation of a <code>Matrix</code>.</p>
 *
 */
abstract class MatrixCorrector {

    private val floatArraySize = 9
    private var matrix: Matrix? = null
    private lateinit var values: FloatArray

    init {
        this(null)
    }

    private operator fun invoke(matrixData: Matrix?) {
        matrix = matrixData
        values = FloatArray(floatArraySize)
    }

    open fun performAbsoluteCorrections() {}

    /**
     *
     * Returns the corrected value of the given relative vector.
     * @param vector
     * @param x
     * @return
     */
    fun correctRelative(vector: Int, x: Float): Float {
        val v = getValues()[vector]
        return when (vector) {
            Matrix.MTRANS_X, Matrix.MTRANS_Y -> correctAbsolute(
                vector,
                v + x
            ) - v
            Matrix.MSCALE_X, Matrix.MSCALE_Y -> correctAbsolute(
                vector,
                v * x
            ) / v
            else -> throw IllegalArgumentException("Vector not supported")
        }
    }

    /**
     * *
     *
     *Returns the corrected value of the given absolute vector.
     * @param vector
     * @param x
     * @return
     */
    internal open fun correctAbsolute(vector: Int, x: Float): Float {
        return x
    }

    /**
     * *
     *
     *Returns the matrix.
     * @return
     */
    open fun getMatrix(): Matrix? {
        return matrix
    }

    /**
     * * *
     *
     *Sets the matrix.
     * @param matrix
     */
    open fun setMatrix(matrix: Matrix?) {
        this.matrix = matrix
    }

    /**
     * * *
     *
     *Returns the matrix values.
     * @return
     */
    fun getValues(): FloatArray {
        matrix?.getValues(values)
        return values
    }
}
