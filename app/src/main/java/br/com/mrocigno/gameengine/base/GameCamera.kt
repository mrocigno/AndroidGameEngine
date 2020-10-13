package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import br.com.mrocigno.gameengine.animation.SimpleAnimationController
import br.com.mrocigno.gameengine.animation.Tween
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.logical.OnBoundsChange

class GameCamera(
    private val renderBounds: GameBounds,
    private val engine: GameEngine
): OnBoundsChange {

    var follow: GameDrawable? = null
        set(value) {
            follow?.bounds?.removeObserver(this)
            field = value?.apply {
                oldLeft = bounds.left
                oldTop = bounds.top
                bounds.addObserver(this@GameCamera)
            }
        }

    private var oldLeft = 0f
    private var oldTop = 0f
    private var tween: Tween<PointF>? = null
        set(value) {
            tween?.close()
            field = value
        }
    private val animation = SimpleAnimationController(
        engine,
        durationMillis = 500
    ) { tween?.let { renderBounds.setCenter(it.currentValue) } }

    fun moveTo(point: PointF, smooth: Boolean = true) = moveTo(point.x, point.y, smooth)
    fun moveTo(cx: Float, cy: Float, smooth: Boolean = true) {
        follow = null
        if (smooth) {
            tween = Tween(
                renderBounds.getCenter(),
                PointF(cx + renderBounds.left, cy + renderBounds.top)
            ).sync(animation)
            animation.start()
        } else {
            renderBounds.setCenter(cx + renderBounds.left, cy + renderBounds.top)
        }
    }

    fun draw(canvas: Canvas, component: GameDrawable) {
        component.bounds.offset(renderBounds.left * -1, renderBounds.top * -1)
        if (component.bounds.intersects(engine.windowBounds!!)) {
            component.draw(canvas)
        }
    }

    //region OnBoundsChange implements
    override fun onChange(rect: RectF) {
        val offsetX = rect.left - oldLeft
        val offsetY = rect.top - oldTop
        renderBounds.literalOffset(offsetX, offsetY)
    }
    //endregion
}