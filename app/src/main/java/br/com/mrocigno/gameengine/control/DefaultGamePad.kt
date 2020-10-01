package br.com.mrocigno.gameengine.control

import android.graphics.Canvas
import android.graphics.Paint
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.utils.toDp

class DefaultGamePad : GamePad(
    dpadRadius = 70f.toDp(),
    directionRadius = 25f.toDp()
) {
    private val stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.toDp()
    }

    private val filled = Paint()

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            centerX, centerY, dpadRadius, stroke
        )

        canvas.drawCircle(
            directionX, directionY, directionRadius, filled
        )
    }
}