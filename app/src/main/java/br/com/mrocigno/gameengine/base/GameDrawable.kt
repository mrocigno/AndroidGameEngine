package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.CallSuper

abstract class GameDrawable(
    protected val engine: GameEngine,
    val hasCollision: Boolean = true
) {

    abstract val bounds: RectF
    private var onClick: ((event: MotionEvent) -> Unit)? = null

    abstract fun draw(canvas: Canvas)

    @CallSuper
    open fun setOnClickListener(onClick: (event: MotionEvent) -> Unit) {
        this.onClick = onClick
    }

    open fun onCollide(hitObject: GameDrawable) {
        Log.d("GameEngine", "Collide with: $hitObject")
    }

    open fun clickIsOnBoundaries(event: MotionEvent): Boolean =
        event.x >= bounds.left && event.x <= bounds.right &&
        event.y >= bounds.top && event.y <= bounds.bottom

    @CallSuper
    open fun performTouch(event: MotionEvent) {
        onClick?.invoke(event)
    }

//    open fun getBounds() = RectF(
//        positionX,
//        positionY,
//        width + positionX,
//        height + positionY
//    )
}