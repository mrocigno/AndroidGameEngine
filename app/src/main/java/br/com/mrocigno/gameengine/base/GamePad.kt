package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import br.com.mrocigno.gameengine.utils.toDp
import kotlin.math.*

abstract class GamePad {

    val gamePadObservers = mutableListOf<OnInteract>()

    @CallSuper
    open fun onInteract(
        interactionType: String,
        radian: Float? = null,
        velocity: Float? = null,
        axis: GamePadAxis? = null
    ) {
        val helper = radian?.let { rd ->
            velocity?.let { vl ->
                axis?.let { ax ->
                    JoystickHelper(rd, vl, ax)
                }
            }
        }
        gamePadObservers.forEach { it.onInteract(interactionType, helper) }
    }

    abstract fun onTouchListener(view: View, event: MotionEvent)

    abstract fun draw(canvas: Canvas)

    interface OnInteract {
        fun onInteract(
            interactionType: String,
            joystickHelper: JoystickHelper? = null
        )
    }
}

enum class GamePadAxis {
    NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST
}

class Joystick(
    private val dpadRadius: Float = 70f.toDp(),
    private val directionRadius: Float = 25f.toDp(),
    private val filled: Paint = Paint(),
    private val stroke: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.toDp()
    }
) {

    var draw: Boolean = false
    val hasId: Boolean get() = joystickId != -1
    var joystickId: Int = -1
        set(value) {
            draw = value != -1
            field = value
        }

    private var joystickCenterX: Float = 0f
    private var joystickCenterY: Float = 0f
    private var joystickDirectionX: Float = 0f
    private var joystickDirectionY: Float = 0f

    fun getRadian() : Float {
        val padX = (joystickDirectionX - joystickCenterX)
        val padY = (joystickDirectionY - joystickCenterY)
        val radian = atan(padY / padX)
        return if (radian.isNaN()) 0f else radian
    }

    fun getVelocity() : Float {
        val deltaX = (joystickCenterX - joystickDirectionX).pow(2)
        val deltaY = (joystickCenterY - joystickDirectionY).pow(2)
        val distance = sqrt(deltaX + deltaY)
        return min(distance / dpadRadius, 1f)
    }

    fun getAxis() : GamePadAxis {
        val padX = (joystickDirectionX - joystickCenterX)
        val padY = (joystickDirectionY - joystickCenterY)

        return when {
            padX >= 0 && padY < 0 -> GamePadAxis.NORTHEAST
            padX >= 0 && padY >= 0 -> GamePadAxis.SOUTHEAST
            padX < 0 && padY > 0 -> GamePadAxis.SOUTHWEST
            padX < 0 && padY <= 0 -> GamePadAxis.NORTHWEST
            else -> GamePadAxis.NORTHEAST
        }
    }

    fun matchIndex(index: Int, event: MotionEvent) =
        event.findPointerIndex(joystickId) == index

    fun setCenter(centerX: Float, centerY: Float) {
        joystickCenterX = centerX
        joystickCenterY = centerY
    }

    fun setDirection(directionX: Float, directionY: Float) {
        val deltaX = (joystickCenterX - directionX).pow(2)
        val deltaY = (joystickCenterY - directionY).pow(2)
        val distance = sqrt(deltaX + deltaY)

        var newX = directionX
        var newY = directionY
        if (distance >= dpadRadius) {
            newX = joystickCenterX + (directionX - joystickCenterX) * (dpadRadius / distance)
            newY = joystickCenterY + (directionY - joystickCenterY) * (dpadRadius / distance)
        }

        joystickDirectionX = newX
        joystickDirectionY = newY
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(
            joystickCenterX, joystickCenterY, dpadRadius, stroke
        )

        if (joystickDirectionX > 0 && joystickDirectionY > 0){
            canvas.drawCircle(
                joystickDirectionX, joystickDirectionY, directionRadius, filled
            )
        }
    }

    fun clearId() {
        joystickDirectionX = 0f
        joystickDirectionY = 0f
        joystickCenterX = 0f
        joystickCenterY = 0f
        joystickId = -1
    }
}

class JoystickHelper(
    val radian: Float,
    val velocity: Float,
    val axis: GamePadAxis
) {

    var distance = 15f * velocity
        set(value) {
            acresX = value * cos(radian)
            acresY = sqrt(value.pow(2) - acresX.pow(2))
            field = value
        }

    private var acresX = distance * cos(radian)
    private var acresY = sqrt(distance.pow(2) - acresX.pow(2))

    fun getDegrees() = (radian * 180f / Math.PI).toFloat() + when (axis) {
        GamePadAxis.NORTHEAST -> 90
        GamePadAxis.SOUTHEAST -> 90
        GamePadAxis.SOUTHWEST -> 270
        GamePadAxis.NORTHWEST -> 270
    }


    fun nextRect(base: RectF): RectF = when (axis) {
        GamePadAxis.NORTHEAST -> {
            getNextPosition(base.left + acresX, base.top - acresY, base)
        }
        GamePadAxis.SOUTHEAST -> {
            getNextPosition(base.left + acresX, base.top + acresY, base)
        }
        GamePadAxis.NORTHWEST -> {
            getNextPosition(base.left - acresX, base.top - acresY, base)
        }
        GamePadAxis.SOUTHWEST -> {
            getNextPosition(base.left - acresX, base.top + acresY, base)
        }
    }

    private fun getNextPosition(nextX: Float, nextY: Float, bounds: RectF) = RectF(
        nextX, nextY, nextX + bounds.width(), nextY + bounds.height()
    )

}