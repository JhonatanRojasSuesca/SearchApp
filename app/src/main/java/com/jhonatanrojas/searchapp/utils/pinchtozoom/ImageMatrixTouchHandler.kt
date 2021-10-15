package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import kotlin.math.pow

/**
 * <p>The <code>ImageMatrixTouchHandler</code> enables pinch-zoom,
 * pinch-rotate and dragging on an <code>ImageView</code>.
 * Registering an instance of this class to an
 * <code>ImageView</code> is the only thing you need to do.</p>
 *
 * TODO Make event methods (for easy overriding)
 */
class ImageMatrixTouchHandler(context: Context?) : MultiTouchListener() {

    private var corrector: ImageMatrixCorrector? = null
    private var savedMatrix: Matrix? = null
    private var mode = 0
    private var startMid: PointF? = null
    private var mid: PointF? = null
    private var startSpacing = 0f
    private var startAngle = 0f
    private var pinchVelocity = 0f
    private var rotateEnabled = false
    private var scaleEnabled = false
    private var translateEnabled = false
    private var dragOnPinchEnabled = false
    private var doubleTapZoomDuration: Long = 0
    private var flingDuration: Long = 0
    private var zoomReleaseDuration: Long = 0
    private var pinchVelocityWindow: Long = 0
    private var doubleTapZoomFactor = 0f
    private var doubleTapZoomOutFactor = 0f
    private var flingExaggeration = 0f
    private var zoomReleaseExaggeration = 0f
    private var updateTouchState = false
    private var gestureDetector: GestureDetector? = null
    private var valueAnimator: ValueAnimator? = null

    private val imageGestureListener = object : SimpleOnGestureListener() {

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (mode == DRAG) {
                if (flingDuration > 0 && !isAnimating()) {
                    val factor: Float =
                        flingDuration.toFloat() / 1000f * flingExaggeration
                    val values: FloatArray = corrector!!.getValues()
                    val dx =
                        velocityX * factor * values[Matrix.MSCALE_X]
                    val dy =
                        velocityY * factor * values[Matrix.MSCALE_Y]
                    val flingX = PropertyValuesHolder.ofFloat(
                        FlingAnimatorHandler.PROPERTY_TRANSLATE_X,
                        values[Matrix.MTRANS_X],
                        values[Matrix.MTRANS_X] + dx
                    )
                    val flingY = PropertyValuesHolder.ofFloat(
                        FlingAnimatorHandler.PROPERTY_TRANSLATE_Y,
                        values[Matrix.MTRANS_Y],
                        values[Matrix.MTRANS_Y] + dy
                    )
                    valueAnimator = ValueAnimator.ofPropertyValuesHolder(flingX, flingY)
                    valueAnimator?.duration = flingDuration
                    valueAnimator?.addUpdateListener(FlingAnimatorHandler(corrector))
                    valueAnimator?.interpolator = DecelerateInterpolator()
                    valueAnimator?.start()
                    return true
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            if (doubleTapZoomFactor > 0 && !isAnimating()) {
                val sx: Float = corrector?.getValues()?.get(Matrix.MSCALE_X) ?: 0f
                val innerFitScale: Float = corrector!!.innerFitScale
                val reversalScale: Float = innerFitScale * doubleTapZoomOutFactor
                val scaleAnimatorHandler =
                    ScaleAnimatorHandler(corrector, e.x, e.y)
                val scaleTo =
                    if (sx > reversalScale) innerFitScale else sx * doubleTapZoomFactor
                animateZoom(sx, scaleTo, doubleTapZoomDuration, scaleAnimatorHandler, null)
                return true
            }
            return super.onDoubleTap(e)
        }
    }

    init {
        this(context, ImageViewerCorrector())
    }

    private operator fun invoke(context: Context?, imageCorrector: ImageMatrixCorrector) {
        corrector = imageCorrector
        savedMatrix = Matrix()
        mode = NONE
        startMid = PointF()
        mid = PointF()
        startSpacing = 1f
        startAngle = 0f
        rotateEnabled = false
        scaleEnabled = true
        translateEnabled = true
        dragOnPinchEnabled = true
        pinchVelocityWindow = 100
        doubleTapZoomDuration = 200
        flingDuration = 200
        zoomReleaseDuration = 200
        zoomReleaseExaggeration = 1.337f
        flingExaggeration = 0.1337f
        doubleTapZoomFactor = 2.5f
        doubleTapZoomOutFactor = 1.4f
        gestureDetector = GestureDetector(context, imageGestureListener)
        gestureDetector?.setOnDoubleTapListener(imageGestureListener)
    }

    /**
     *
     * Indicates whether the image is being animated.
     *
     * @return
     */
    fun isAnimating(): Boolean {
        return valueAnimator != null && valueAnimator?.isRunning!!
    }

    /**
     *
     * Cancels any running animations.
     */
    fun cancelAnimation() {
        if (isAnimating()) {
            valueAnimator?.cancel()
        }
    }

    /**
     *
     * Evaluates the touch state.
     *
     * @param event
     * @param matrix
     */
    private fun evaluateTouchState(
        event: MotionEvent,
        matrix: Matrix
    ) {
        // Save the starting points
        updateStartPoints(event)
        savedMatrix?.set(matrix)

        // Update the mode
        val touchCount = touchCount
        if (touchCount == 0) {
            mode = NONE
        } else {
            if (isAnimating()) {
                valueAnimator?.cancel()
            }
            if (touchCount == 1) {
                if (mode == PINCH) {
                    if (zoomReleaseDuration > 0 && !isAnimating()) {
                        // Animate zoom release
                        val scale = pinchVelocity.toDouble()
                            .pow(1.0 / 1000.0)
                            .pow(zoomReleaseDuration.toDouble())
                            .pow(zoomReleaseExaggeration.toDouble()).toFloat()
                        mid?.let {
                            animateZoom(
                                scale,
                                zoomReleaseDuration,
                                it.x,
                                it.y,
                                DecelerateInterpolator()
                            )
                        }
                    }
                }
                mode = DRAG
            } else if (touchCount > 1) {
                mode = PINCH

                // Calculate the start distance
                startSpacing = spacing(event, getId(0), getId(1))
                pinchVelocity = 0f
                if (startSpacing > MIN_PINCH_DIST_PIXELS) {
                    startMid?.let { midPoint(it, event, getId(0), getId(1)) }
                    startAngle = angle(
                        event,
                        getId(0),
                        getId(1),
                        startedLower(getStartPoint(0), getStartPoint(1))
                    )
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        super.onTouch(view, event)
        gestureDetector?.onTouchEvent(event)
        val imageView: ImageView
        imageView = try {
            view as ImageView
        } catch (e: ClassCastException) {
            throw IllegalStateException("View must be an instance of ImageView", e)
        }
        // Get the matrix
        val matrix = imageView.imageMatrix
        // Sets the image view
        if (corrector?.getImageView() !== imageView) {
            corrector?.setImageView(imageView)
        } else if (imageView.scaleType != ImageView.ScaleType.MATRIX) {
            imageView.scaleType = ImageView.ScaleType.MATRIX
            corrector?.setMatrix(matrix)
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> evaluateTouchState(
                event,
                matrix
            )
            MotionEvent.ACTION_MOVE -> {
                if (updateTouchState) {
                    evaluateTouchState(event, matrix)
                    updateTouchState = false
                }
                // Reuse the saved matrix
                matrix.set(savedMatrix)
                if (mode == DRAG) {
                    if (translateEnabled) {
                        // Get the start point
                        val start = getStartPoint(0)
                        val index = event.findPointerIndex(getId(0))
                        var dx = event.getX(index) - start.x
                        dx = corrector?.correctRelative(Matrix.MTRANS_X, dx)!!
                        var dy = event.getY(index) - start.y
                        dy = this.corrector?.correctRelative(Matrix.MTRANS_Y, dy)!!
                        matrix.postTranslate(dx, dy)
                    }
                } else if (mode == PINCH) {
                    // Get the new midpoint
                    mid?.let { midPoint(it, event, getId(0), getId(1)) }
                    // Rotate
                    if (rotateEnabled) {
                        val deg = startAngle - angle(
                            event,
                            getId(0),
                            getId(1),
                            startedLower(getStartPoint(0), getStartPoint(1))
                        )
                        mid?.let { matrix.postRotate(deg, it.x, it.y) }
                    }
                    if (scaleEnabled) {
                        // Scale
                        val spacing =
                            spacing(event, getId(0), getId(1))
                        var sx = spacing / startSpacing
                        sx = corrector?.correctRelative(Matrix.MSCALE_X, sx)!!
                        mid?.let { matrix.postScale(sx, sx, it.x, it.y) }
                        if (event.historySize > 0) {
                            pinchVelocity = pinchVelocity(
                                event,
                                getId(0),
                                getId(1),
                                pinchVelocityWindow
                            )
                        }
                    }
                    if (dragOnPinchEnabled && translateEnabled) {
                        // Translate
                        val dx = mid!!.x - startMid!!.x
                        val dy = mid!!.y - startMid!!.y
                        matrix.postTranslate(dx, dy)
                    }
                    corrector?.performAbsoluteCorrections()
                }
                imageView.invalidate()
            }
        }
        return true // indicate event was handled
    }

    /**
     *
     * Performs a zoom animation using the given `zoomFactor` and centerpoint coordinates.
     *
     * @param zoomFactor
     * @param duration
     * @param x
     * @param y
     * @param interpolator
     */
    private fun animateZoom(
        zoomFactor: Float,
        duration: Long,
        x: Float,
        y: Float,
        interpolator: Interpolator?
    ) {
        val sx = corrector?.getValues()?.get(Matrix.MSCALE_X)
        sx?.let {
            animateZoom(
                it,
                sx * zoomFactor,
                duration,
                ScaleAnimatorHandler(corrector, x, y),
                interpolator
            )
        }
    }

    /**
     *
     * Performs a zoom animation from `scaleFrom` to `scaleTo` using the given `ScaleAnimatorHandler`.
     *
     * @param scaleFrom
     * @param scaleTo
     * @param duration
     * @param scaleAnimatorHandler
     * @param interpolator
     */
    private fun animateZoom(
        scaleFrom: Float,
        scaleTo: Float,
        duration: Long,
        scaleAnimatorHandler: ScaleAnimatorHandler,
        interpolator: Interpolator?
    ) {
        check(!isAnimating()) { "An animation is currently running; Check isAnimating() first!" }
        valueAnimator = ValueAnimator.ofFloat(scaleFrom, scaleTo)
        valueAnimator?.duration = duration
        valueAnimator?.addUpdateListener(scaleAnimatorHandler)
        if (interpolator != null) valueAnimator?.interpolator = interpolator
        valueAnimator?.start()
    }

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val PINCH = 2
        private const val MIN_PINCH_DIST_PIXELS = 10f
    }
}
