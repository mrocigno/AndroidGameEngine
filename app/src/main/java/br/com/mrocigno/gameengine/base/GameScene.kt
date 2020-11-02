package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.CallSuper
import br.com.mrocigno.gameengine.Shoot
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.logical.OnBoundsChange
import br.com.mrocigno.gameengine.animation.SimpleAnimationController
import br.com.mrocigno.gameengine.animation.Tween
import java.util.*

abstract class GameScene(val engine: GameEngine) {

    private lateinit var components: MutableSet<GameDrawable>
    private lateinit var collisionObjects: MutableSet<GameDrawable>
    protected val camera: GameCamera = GameCamera(engine.windowBounds!!.copy(), engine)

    @CallSuper
    open fun onCreate() {
        components = getComponents()
        collisionObjects = components.filter { it.hasCollision }.toMutableSet()
        engine.gamePad?.gamePadObservers?.addAll(getControllable())
    }

    @CallSuper
    open fun draw(canvas: Canvas) {
        components.forEach {
            if (it.hasCollision && collisionObjects.isNotEmpty()) {
                synchronized(collisionObjects) {
                    for (hitObject in collisionObjects) {
                        if (it != hitObject && it.matchBound(hitObject.bounds) && hitObject.matchBound(it.bounds)) {
                            hitObject.onCollide(it)
                            it.onCollide(hitObject)
                        }
                    }
                }
            }
            camera.draw(canvas, it)
        }
    }

    fun onTouch(event: MotionEvent) {
        components.lastOrNull {
            val isClickable = it.clickIsOnBoundaries(event)
            if (isClickable) it.performTouch(event)
            isClickable
        }
    }

    fun addCollideObject(drawable: GameDrawable) {
        if (drawable.hasCollision) synchronized(collisionObjects) {
            collisionObjects.add(drawable)
        }
    }

    fun removeCollideObject(drawable: GameDrawable) {
        if (drawable.hasCollision) synchronized(collisionObjects) {
            collisionObjects.add(drawable)
        }
    }

    protected abstract fun getComponents(): MutableSet<GameDrawable>

    private fun getControllable(): Collection<GamePad.OnInteract> =
        components.filterIsInstance(GamePad.OnInteract::class.java)
}