package br.com.mrocigno.gameengine.base

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper

abstract class GamePad {

    val gamePadObservers = mutableListOf<OnInteract>()

    @CallSuper
    open fun onInteract(
        interactionType: String,
        radian: Float? = null,
        velocity: Float? = null,
        axis: GamePadAxis? = null
    ) = gamePadObservers.forEach { it.onInteract(interactionType, radian, velocity, axis) }

    abstract fun onTouchListener(view: View, event: MotionEvent)

    abstract fun draw(canvas: Canvas)

    interface OnInteract {
        fun onInteract(
            interactionType: String,
            radian: Float? = null,
            velocity: Float? = null,
            axis: GamePadAxis? = null
        )
    }
}

enum class GamePadAxis {
    NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST
}