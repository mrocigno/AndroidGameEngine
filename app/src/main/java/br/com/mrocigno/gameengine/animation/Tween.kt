package br.com.mrocigno.gameengine.animation

import android.graphics.PointF
import br.com.mrocigno.gameengine.base.GameAnimationController

class Tween<T>(
    private val begin: T,
    private val end: T
) {

    var currentValue: T = begin

    private lateinit var syncWith: GameAnimationController
    private val observer =  { it: Float ->
        process(it)
    }

    fun sync(controller: GameAnimationController): Tween<T> {
        controller.addUpdateListener(observer)
        return this
    }

    private fun process(fraction: Float) {
        if (begin is Float && end is Float) {
            currentValue = (begin + (end - begin) * fraction) as T
        }
        if (begin is PointF && end is PointF) {
            currentValue = PointF(
                begin.x + (end.x - begin.x) * fraction,
                begin.y + (end.y - begin.y) * fraction
            ) as T
        }
    }

    fun close() {
        if (this::syncWith.isInitialized) {
            syncWith.removeUpdateListener(observer)
        }
    }
}