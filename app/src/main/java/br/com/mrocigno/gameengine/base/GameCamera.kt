package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import br.com.mrocigno.gameengine.animation.SimpleAnimationController
import br.com.mrocigno.gameengine.animation.Tween
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.logical.OnBoundsChange
import br.com.mrocigno.gameengine.utils.stayInside

class GameCamera(
    val renderBounds: GameBounds,
    private val engine: GameEngine
): OnBoundsChange {

    var follow: GameDrawable? = null
        set(value) {
            follow?.bounds?.removeObserver(this)
            field = value?.apply {
                bounds.addObserver(this@GameCamera)
            }
        }

    private val cameraLimit = RectF(-2000f, -2000f, 2000f, 2000f)
    private var tween: Tween<PointF>? = null
    private val animation = SimpleAnimationController(engine, 100) {
        tween?.let { moveCamera(it.currentValue.x, it.currentValue.y) }
    }

    fun moveTo(bounds: GameBounds, smooth: Boolean = true) = moveTo(bounds.centerX(), bounds.centerY(), smooth)
    fun moveTo(point: PointF, smooth: Boolean = true) = moveTo(point.x, point.y, smooth)
    fun moveTo(cx: Float, cy: Float, smooth: Boolean = true) {
        if (smooth && animation.isRunning.not()) {
            tween = Tween(
                renderBounds.getCenter(),
                PointF(cx + renderBounds.left, cy + renderBounds.top)
            ).sync(animation)
            animation.start()
        } else {
            moveCamera(cx + renderBounds.left, cy + renderBounds.top)
        }
    }

    private fun moveCamera(x: Float, y: Float) {
        renderBounds.setCenter(x, y)
        renderBounds stayInside cameraLimit
    }

    fun draw(canvas: Canvas, component: GameDrawable) {
        component.bounds.offset(renderBounds.left * -1, renderBounds.top * -1)
        if (component.bounds.intersects(engine.windowBounds!!)) {
            component.draw(canvas)
        }
    }

    //region OnBoundsChange implements
    override fun onChange(rect: RectF) {
        moveTo(rect.centerX(), rect.centerY(), true)
    }
    //endregion
}