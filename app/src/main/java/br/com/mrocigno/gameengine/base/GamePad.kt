package br.com.mrocigno.gameengine.base

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
) : GameDrawable() {

    val gamePadObservers = mutableListOf<OnMove>()

    protected var centerX = 0f
    protected var centerY = 0f
    protected var directionX = 0f
    protected var directionY = 0f

    @CallSuper
    open fun onMove() {
        val angle = getRadian()
        val velocity = getVelocity()
        val axis = getAxis()
        gamePadObservers.forEach { it.onMove(angle, velocity, axis) }
    }

    @CallSuper
    open fun onRelease() {
        gamePadObservers.forEach { it.onRelease() }
    }

    fun setCardinals(centerX: Float, centerY: Float) {
        this.centerX = centerX
        this.centerY = centerY
        this.directionX = centerX
        this.directionY = centerY
    }

    fun setDirection(directionX: Float, directionY: Float) {
        val deltaX = (centerX - directionX).pow(2)
        val deltaY = (centerY - directionY).pow(2)
        val distance = sqrt(deltaX + deltaY)

        var newX = directionX
        var newY = directionY
        if (distance >= dpadRadius) {
            newX = centerX + (directionX - centerX) * (dpadRadius / distance)
            newY = centerY + (directionY - centerY) * (dpadRadius / distance)
        }

        this.directionX = newX
        this.directionY = newY
    }

    private fun getRadian() : Float {
        val padX = (directionX - centerX)
        val padY = (directionY - centerY)
        val radian = atan(padY / padX)
        return if (radian.isNaN()) 0f else radian
    }

    private fun getVelocity() : Float {
        val deltaX = (centerX - directionX).pow(2)
        val deltaY = (centerY - directionY).pow(2)
        val distance = sqrt(deltaX + deltaY)
        return min(distance / dpadRadius, 1f)
    }

    private fun getAxis() : GamePadAxis {
        val padX = (directionX - centerX)
        val padY = (directionY - centerY)

        return when {
            padX >= 0 && padY < 0 -> GamePadAxis.NORTHEAST
            padX >= 0 && padY >= 0 -> GamePadAxis.SOUTHEAST
            padX < 0 && padY > 0 -> GamePadAxis.SOUTHWEST
            padX < 0 && padY <= 0 -> GamePadAxis.NORTHWEST
            else -> GamePadAxis.NORTHEAST
        }
    }

    abstract fun onTouchListener(view: View, event: MotionEvent)

    interface OnMove {
        fun onMove(radian: Float, velocity: Float, axis: GamePadAxis)
        fun onRelease()
    }
}

enum class GamePadAxis {
    NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST
}