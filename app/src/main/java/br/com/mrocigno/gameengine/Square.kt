package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.tools.GameLoop
import br.com.mrocigno.gameengine.tools.SimpleTicker
import br.com.mrocigno.gameengine.utils.toDp

class Square(engine: GameEngine, x: Float? = null, color: Int) : GameDrawable(engine) {

    override val bounds = RectF(
        x ?: 100f.toDp(),
        100f.toDp(),
        (x ?: 100f.toDp()) + 20f.toDp(),
        120f.toDp()
    )

    private val paint = Paint().apply {
        this.color = color
    }

    fun expandAnimation() {
        bounds.offset(200f.toDp(),  0f)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(
            bounds,
            paint
        )
    }

    override fun toString(): String {
        return "Square $bounds"
    }
}