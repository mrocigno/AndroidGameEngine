package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent
import androidx.annotation.CallSuper
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.logical.OnBoundsChange
import br.com.mrocigno.gameengine.animation.SimpleAnimationController
import br.com.mrocigno.gameengine.animation.Tween

abstract class GameScene(protected val engine: GameEngine) {

    private lateinit var components: List<GameDrawable>
    private lateinit var collisionObjects: List<GameDrawable>
    lateinit var camera: GameCamera

    @CallSuper
    open fun onCreate() {
        components = getComponents()
        collisionObjects = components.filter { it.hasCollision }
        engine.gamePad?.gamePadObservers?.addAll(getControllable())
        camera = initCamera()
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

    private fun matchBounds(bounds1: RectF, bounds2: RectF): Boolean = bounds1.intersects(
        bounds2.left, bounds2.top, bounds2.right, bounds2.bottom
    )

    protected abstract fun getComponents(): List<GameDrawable>

    protected open fun initCamera(): GameCamera = GameCamera(engine.windowBounds!!.copy(), engine)

    private fun getControllable(): Collection<GamePad.OnInteract> =
        components.filterIsInstance(GamePad.OnInteract::class.java)
}