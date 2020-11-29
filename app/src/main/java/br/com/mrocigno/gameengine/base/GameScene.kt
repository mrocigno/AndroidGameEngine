package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.view.MotionEvent
import androidx.annotation.CallSuper
import br.com.mrocigno.gameengine.logical.Collision

abstract class GameScene(val engine: GameEngine) {

    val camera: GameCamera = GameCamera(engine.windowBounds!!.copy(), engine)
    private var collisionObjects: Set<Collision> = emptySet()
    private var components: Set<GameDrawable> = emptySet()
        set(value) {
            field = value
            collisionObjects = components.filterIsInstance(Collision::class.java).toSet()
        }

    @CallSuper
    open fun onCreate() {
        components = getComponents()
        engine.gamePad?.gamePadObservers?.addAll(getControllable())
    }

    @CallSuper
    open fun draw(canvas: Canvas) {
        for (component in components) {
            if (component is Collision) {
                for (hitObject in collisionObjects) {
                    if (component != hitObject && hitObject.matchBound(component.bounds)) {
                        hitObject.onCollide(component)
                    }
                }
            }
            camera.draw(canvas, component)
        }
    }

    fun onTouch(event: MotionEvent) {
        components.lastOrNull {
            val isClickable = it.clickIsOnBoundaries(event)
            if (isClickable) it.performTouch(event)
            isClickable
        }
    }

    fun addComponent(drawable: GameDrawable) {
        components = components.plus(drawable)
    }

    fun removeComponent(drawable: GameDrawable) {
        components = components.minus(drawable)
    }

    protected abstract fun getComponents(): Set<GameDrawable>

    private fun getControllable(): Collection<GamePad.OnInteract> =
        components.filterIsInstance(GamePad.OnInteract::class.java)
}