package br.com.mrocigno.gameengine.animation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.PointF
import br.com.mrocigno.gameengine.base.GameAnimationController

class Tween<T>(
    private val begin: T,
    private val end: T
) {

    var currentValue: T = begin
    private val argbEvaluator by lazy { ArgbEvaluator() }

    private val observer =  { it: Float ->
        process(it)
    }

    fun sync(controller: GameAnimationController): Tween<T> {
        controller.addUpdateListener(observer)
        return this
    }

    fun stop(controller: GameAnimationController) {
        controller.removeUpdateListener(observer)
    }

    private fun process(fraction: Float) {
        when {
            (begin is Float && end is Float) -> {
                currentValue = (begin + (end - begin) * fraction) as T
            }
            (begin is PointF && end is PointF) -> {
                currentValue = PointF(
                    begin.x + (end.x - begin.x) * fraction,
                    begin.y + (end.y - begin.y) * fraction
                ) as T
            }
            (begin is Int && end is Int) -> {
                currentValue = argbEvaluator.evaluate(fraction, begin, end) as T
            }
        }
    }
}