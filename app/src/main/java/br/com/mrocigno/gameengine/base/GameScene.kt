package br.com.mrocigno.gameengine.base

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent

abstract class GameScene(protected val engine: GameEngine){

    abstract val components: List<GameDrawable>

    abstract fun draw(canvas: Canvas)

    fun onTouch(event: MotionEvent) {
        components.lastOrNull {
            val isClickable = it.clickIsOnBoundaries(event)
            if (isClickable) it.performTouch(event)
            isClickable
        }
    }

}