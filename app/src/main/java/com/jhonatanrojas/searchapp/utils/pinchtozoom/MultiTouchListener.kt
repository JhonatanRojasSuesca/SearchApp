package com.jhonatanrojas.searchapp.utils.pinchtozoom

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import java.util.ArrayList
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * <p>This class enables easy interpretation of multitouch gestures such as pinching, rotating etc.</p>
 *
 * TODO Implement convex hull algorithm
 * TODO Implement touch grouping by evaluating touch proximity
 * TODO Implement get touch numbers ordered by x- or y-axis alignment
 *
 */
open class MultiTouchListener : OnTouchListener {

    private val pointerIds: MutableList<Int>
    private val startPoints: SparseArray<PointF>
    private val initialCapacity = 40

    init {
        pointerIds = ArrayList(initialCapacity)
        startPoints = SparseArray()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        // Separate action and pointer index
        val actionMasked = event.actionMasked
        val actionIndex = event.actionIndex
        val pointerId: Int
        when (actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                pointerId = event.getPointerId(actionIndex)
                val startPoint = PointF(event.getX(actionIndex), event.getY(actionIndex))

                // Save the starting point
                startPoints.put(pointerId, startPoint)
                pointerIds.add(pointerId)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                pointerId = event.getPointerId(actionIndex)
                pointerIds.remove(pointerId)
                startPoints.remove(pointerId)
            }
            MotionEvent.ACTION_CANCEL -> {
                clearPointerIds()
                startPoints.clear()
            }
        }
        return false
    }

    /**
     *
     * Clears all registered pointer ids.
     */
    private fun clearPointerIds() {
        pointerIds.clear()
    }

    /**
     *
     * Returns the current amount of touch points.
     * @return
     */
    val touchCount: Int
        get() = pointerIds.size

    /**
     *
     * Returns the pointer id for the given index of subsequent touch points.
     * @param touchNo
     * @return
     */
    fun getId(touchNo: Int): Int {
        return pointerIds[touchNo]
    }

    /**
     *
     * Returns the start point for the given touch number (where the user initially pressed down).
     * @param touchNo
     * @return
     */
    fun getStartPoint(touchNo: Int): PointF {
        return startPoints[getId(touchNo)]
    }

    /**
     *
     * Updates the start points with the current coordinate configuration.
     * @param event
     */
    fun updateStartPoints(event: MotionEvent) {
        var startPoint: PointF
        var pointerId: Int
        var i = 0
        val n = event.pointerCount
        while (i < n) {
            pointerId = event.getPointerId(i)
            startPoint = PointF(event.getX(i), event.getY(i))

            // Save the starting point
            startPoints.put(pointerId, startPoint)
            i++
        }
    }

    /**
     *
     * Calculates the space between two pointers.
     * @param event
     * @param pointerA id of pointer A
     * @param pointerB id of pointer B
     * @return spacing between both pointers
     */
    fun spacing(event: MotionEvent, pointerA: Int, pointerB: Int): Float {
        val indexA = event.findPointerIndex(pointerA)
        val indexB = event.findPointerIndex(pointerB)
        return spacingByIndex(event, indexA, indexB)
    }

    /**
     *
     * @param event
     * @param indexA
     * @param indexB
     * @return
     */
    private fun spacingByIndex(event: MotionEvent, indexA: Int, indexB: Int): Float {
        val x = event.getX(indexA) - event.getX(indexB)
        val y = event.getY(indexA) - event.getY(indexB)
        return sqrt(x * x + y * y.toDouble()).toFloat() // Pythagoras
    }

    /**
     *
     * Calculates the pinch velocity for the last `timeWindow` milliseconds.
     * @param event
     * @param pointerA id of pointer A
     * @param pointerB id of pointer B
     * @param timeWindow
     * @return spacing between both pointers
     */
    fun pinchVelocity(
        event: MotionEvent,
        pointerA: Int,
        pointerB: Int,
        timeWindow: Long
    ): Float {
        val indexA = event.findPointerIndex(pointerA)
        val indexB = event.findPointerIndex(pointerB)
        val eventTime = event.eventTime
        var timeDelta: Long = 0
        var previousSpacing = spacingByIndex(event, indexA, indexB)
        var scale = 1f
        var i = 0
        val n = event.historySize
        while (i < n && timeDelta < timeWindow) {
            val index = n - 1 - i
            val x =
                event.getHistoricalX(indexA, index) - event.getHistoricalX(indexB, index)
            val y =
                event.getHistoricalY(indexA, index) - event.getHistoricalY(indexB, index)
            val spacing =
                sqrt(x * x + y * y.toDouble()).toFloat()
            scale *= previousSpacing / spacing
            previousSpacing = spacing
            timeDelta = eventTime - event.getHistoricalEventTime(index)
            i++
        }
        return scale.toDouble().pow(1.0 / timeWindow).pow(1000.0).toFloat()
    }

    /**
     *
     * Calculates the mid point between two pointers.
     * @param point
     * @param event
     * @param pointerA id of pointer A
     * @param pointerB id of pointer B
     */
    fun midPoint(point: PointF, event: MotionEvent, pointerA: Int, pointerB: Int) {
        val indexA = event.findPointerIndex(pointerA)
        val indexB = event.findPointerIndex(pointerB)
        val x = event.getX(indexA) + event.getX(indexB)
        val y = event.getY(indexA) + event.getY(indexB)
        point[x / 2f] = y / 2f
    }

    /**
     *
     * Calculates the angle between two points.
     * @param event
     * @param pointerA id of pointer A
     * @param pointerB id of pointer B
     * @param isPointerAPivot indicates if pointer A is considered to be the pivot,
     * else pointer B is. Use [.startedLower]
     * @return angle in degrees
     */
    fun angle(
        event: MotionEvent,
        pointerA: Int,
        pointerB: Int,
        isPointerAPivot: Boolean
    ): Float {
        // Resolve the indices
        val indexA = event.findPointerIndex(pointerA)
        val indexB = event.findPointerIndex(pointerB)

        // Get the x-y displacement
        val x = event.getX(indexA) - event.getX(indexB)
        val y = event.getY(indexA) - event.getY(indexB)

        // Calculate the arc tangent
        var atan = atan(x / y.toDouble())

        // Always consider the same pointer the pivot
        if (y < 0f && isPointerAPivot || y > 0f && !isPointerAPivot) {
            atan += Math.PI
        }

        // Convert to float in degrees
        val deg = Math.toDegrees(atan)
        return deg.toFloat()
    }

    /**
     *
     * Convenience method to determine whether starting point
     * A has a lower y-axis value than starting point B.
     * Useful in conjunction with [.angle].
     * @param pointA
     * @param pointB
     * @return
     */
    fun startedLower(pointA: PointF, pointB: PointF): Boolean {
        return pointA.y < pointB.y
    }
}
