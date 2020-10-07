package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import kotlin.math.atan
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

abstract class GamePad(
    protected val dpadRadius: Float,
    protected val directionRadius: Float
) {

    val gamePadObservers = mutableListOf<OnMove>()

    protected var centerX = 0f
    protected var centerY = 0f
    protected var directionX = 0f
    protected var directionY = 0f

    @CallSuper
    open fun onMove(angle: Float, velocity: Float, axis: GamePadAxis) {
        gamePadObservers.forEach { it.onMove(angle, velocity, axis) }
    }

    @CallSuper
    open fun onRelease() {
        gamePadObservers.forEach { it.onRelease() }
    }

    @CallSuper
    open fun onDown() {
        gamePadObservers.forEach { it.onDown() }
    }

    abstract fun onTouchListener(view: View, event: MotionEvent)

    abstract fun draw(canvas: Canvas)

    interface OnMove {
        fun onDown()
        fun onMove(radian: Float, velocity: Float, axis: GamePadAxis)
        fun onRelease()
    }
}

enum class GamePadAxis {
    NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST
}