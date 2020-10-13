package br.com.mrocigno.gameengine

import android.graphics.*
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.utils.getCollisionOutsideNewBounds
import br.com.mrocigno.gameengine.utils.toDp

class Square(engine: GameEngine, x: Float? = null, color: Int) : GameDrawable(engine) {

    override val bounds = GameBounds(
        x ?: 100f.toDp(),
        100f.toDp(),
        (x ?: 100f.toDp()) + 40f.toDp(),
        140f.toDp()
    )

    private val paint = Paint().apply {
        this.color = color
    }

    fun expandAnimation() {
        bounds.rotate(30f, bounds.width()/2, bounds.height() /2)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(
            bounds.path,
            paint
        )
    }

    override fun onCollide(hitObject: GameDrawable) {
        hitObject.bounds.getCollisionOutsideNewBounds(bounds)
    }

    override fun toString(): String {
        return "Square $bounds"
    }
}