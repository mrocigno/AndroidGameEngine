package br.com.mrocigno.gameengine

import android.graphics.*
import android.util.Log
import br.com.mrocigno.gameengine.animation.SimpleAnimationController
import br.com.mrocigno.gameengine.animation.Tween
import br.com.mrocigno.gameengine.base.GameAnimationController
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GameScene
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.utils.getCollisionOutsideNewBounds
import br.com.mrocigno.gameengine.utils.toDp

class Square(engine: GameEngine, x: Float? = null, color: Int) : GameDrawable(engine) {

    private val hitAnimationController: GameAnimationController = SimpleAnimationController(engine, 50) {
        paint.color = colorTransition.currentValue
        bounds.scale = expand.currentValue
    }
    private val colorTransition = Tween(Color.GRAY, color).sync(hitAnimationController)
    private val expand = Tween(1.2f, 1f).sync(hitAnimationController)

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
        super.onCollide(hitObject)
        when(hitObject) {
            is Persona -> hitObject.bounds.getCollisionOutsideNewBounds(bounds)
            is Shoot -> {
                hitObject.dissolve()
                hitAnimation()
            }
        }
    }

    private fun hitAnimation() {
        hitAnimationController.start()
    }

    override fun toString(): String {
        return "Square $bounds"
    }
}