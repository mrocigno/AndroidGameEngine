package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.view.MotionEvent
import androidx.annotation.CallSuper

abstract class GameDrawable {

    abstract var positionX: Float
    abstract var positionY: Float
    abstract var width: Float
    abstract var height: Float

    private var onClick: ((event: MotionEvent) -> Unit)? = null

    abstract fun draw(canvas: Canvas)

    @CallSuper
    open fun setOnClickListener(onClick: (event: MotionEvent) -> Unit) {
        this.onClick = onClick
    }

    open fun clickIsOnBoundaries(event: MotionEvent): Boolean =
        event.x >= positionX && event.x <= (positionX + width) &&
        event.y >= positionY && event.y <= (positionY + height)

    @CallSuper
    open fun performTouch(event: MotionEvent) {
        onClick?.invoke(event)
    }

}