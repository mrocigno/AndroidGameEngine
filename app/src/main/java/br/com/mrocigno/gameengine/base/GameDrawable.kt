package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.CallSuper
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.utils.toDp

abstract class GameDrawable(protected val engine: GameEngine) {

    abstract val bounds: GameBounds
    val scene get() = engine.scene!!

    private var onClick: ((event: MotionEvent) -> Unit)? = null
    private val debugPaint by lazy {
        Paint().apply {
            color = Color.MAGENTA
            style = Paint.Style.STROKE
            strokeWidth = 1.toDp()
        }
    }

    abstract fun onDraw(canvas: Canvas)

    @CallSuper
    open fun setOnClickListener(onClick: (event: MotionEvent) -> Unit) {
        this.onClick = onClick
    }

    open fun clickIsOnBoundaries(event: MotionEvent): Boolean =
        event.x >= bounds.left && event.x <= bounds.right &&
        event.y >= bounds.top && event.y <= bounds.bottom

    @CallSuper
    open fun performTouch(event: MotionEvent) {
        onClick?.invoke(event)
    }

    fun draw(canvas: Canvas) {
        if (engine.debugMode) canvas.drawPath(bounds.path, debugPaint)
        onDraw(canvas)
    }
}