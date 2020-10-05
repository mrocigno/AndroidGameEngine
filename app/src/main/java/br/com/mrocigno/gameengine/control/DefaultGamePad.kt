package br.com.mrocigno.gameengine.control

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.utils.toDp

class DefaultGamePad : GamePad(
    dpadRadius = 70f.toDp(),
    directionRadius = 25f.toDp()
) {

    var draw: Boolean = false

    private val stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.toDp()
    }

    private val filled = Paint()

    override fun onTouchListener(view: View, event: MotionEvent) {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                setCardinals(event.x, event.y)
                draw = true
            }
            MotionEvent.ACTION_MOVE -> {
                this.onMove()
                this.setDirection(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                this.onRelease()
                draw = false
            }
        }
    }

    override var positionX: Float = 0f
    override var positionY: Float = 0f
    override var width: Float = 0f
    override var height: Float = 0f

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