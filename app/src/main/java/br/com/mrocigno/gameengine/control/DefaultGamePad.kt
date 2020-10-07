package br.com.mrocigno.gameengine.control

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.base.GamePadAxis
import br.com.mrocigno.gameengine.utils.toDp
import kotlin.math.atan
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultGamePad : GamePad(
    dpadRadius = 70f.toDp(),
    directionRadius = 25f.toDp()
) {

    var draw: Boolean = false

    private val filled = Paint()
    private val stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.toDp()
    }

    override fun onTouchListener(view: View, event: MotionEvent) {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.onDown()
                setCardinals(event.x, event.y)
                draw = true
            }
            MotionEvent.ACTION_MOVE -> {
                this.onMove(getRadian(), getVelocity(), getAxis())
                this.setDirection(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                this.onRelease()
                draw = false
            }
        }
    }

    private fun setCardinals(centerX: Float, centerY: Float) {
        this.centerX = centerX
        this.centerY = centerY
        this.directionX = centerX
        this.directionY = centerY
    }

    private fun setDirection(directionX: Float, directionY: Float) {
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

    override fun draw(canvas: Canvas) {
        if (draw) {
            canvas.drawCircle(
                centerX, centerY, dpadRadius, stroke
            )

            canvas.drawCircle(
                directionX, directionY, directionRadius, filled
            )
        }
    }
}