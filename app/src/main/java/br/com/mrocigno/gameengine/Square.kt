package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.tools.GameLoop
import br.com.mrocigno.gameengine.tools.SimpleTicker
import br.com.mrocigno.gameengine.utils.toDp

class Square(engine: GameEngine, x: Float? = null, color: Int) : GameDrawable(engine) {

    override var positionX: Float = x ?: 100f.toDp()
    override var positionY: Float = 100f.toDp()
    override var width: Float = 20f.toDp()
    override var height: Float = 20f.toDp()

    private val paint = Paint().apply {
        this.color = color
    }

    fun expandAnimation() {
        val positionX = this.positionX
        val positionY = this.positionY
        val width = this.width
        val height = this.height
        SimpleTicker(engine, 500) {
            this.positionX = positionX - 25f.toDp() * it
            this.positionY = positionY - 25f.toDp() * it
            this.width = width + 50f.toDp() * it
            this.height = height + 50f.toDp() * it
        }.start()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(
            positionX,
            positionY,
            positionX + width,
            positionY + height,
            paint
        )
    }
}