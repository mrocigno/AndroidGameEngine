package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.view.MotionEvent

abstract class GameScene {

    abstract val components: List<GameDrawable>

    abstract fun draw(canvas: Canvas)

    fun onTouch(event: MotionEvent) {
        components.forEach {
            if (it.clickIsOnBoundaries(event)) it.performTouch(event)
        }
    }

}