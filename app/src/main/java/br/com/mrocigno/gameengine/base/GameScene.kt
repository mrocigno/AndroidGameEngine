package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import androidx.annotation.CallSuper

abstract class GameScene(protected val engine: GameEngine){

    private lateinit var components: List<GameDrawable>
    private lateinit var collisionObjects: List<GameDrawable>

    @CallSuper
    open fun onCreate() {
        components = getComponents()
        collisionObjects = components.filter { it.hasCollision }
        engine.gamePad?.gamePadObservers?.addAll(getControllable())
    }

    @CallSuper
    open fun draw(canvas: Canvas) {
        components.forEach {
            if (it.hasCollision && collisionObjects.isNotEmpty()) {
                for (hitObject in collisionObjects) {
                    if (it != hitObject && matchBounds(it.bounds, hitObject.bounds)) {
                        hitObject.onCollide(it)
                        it.onCollide(hitObject)
                    }
                }
            }
            it.draw(canvas)
        }
    }

    private fun matchBounds(bounds1: RectF, bounds2: RectF): Boolean = bounds1.intersects(
        bounds2.left, bounds2.top, bounds2.right, bounds2.bottom
    )

    fun onTouch(event: MotionEvent) {
        components.lastOrNull {
            val isClickable = it.clickIsOnBoundaries(event)
            if (isClickable) it.performTouch(event)
            isClickable
        }
    }

    protected abstract fun getComponents(): List<GameDrawable>

    private fun getControllable(): Collection<GamePad.OnInteract> =
        components.filterIsInstance(GamePad.OnInteract::class.java)
}