package br.com.mrocigno.gameengine.control

import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.base.Joystick

class DefaultGamePad : GamePad() {

    private val joystick = Joystick()

    override fun onTouchListener(view: View, event: MotionEvent) {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.onInteract(ON_DOWN)
                joystick.setCenter(event.x, event.y)
                joystick.draw = true
            }
            MotionEvent.ACTION_MOVE -> {
                this.onInteract(ON_MOVE, joystick.getRadian(), joystick.getVelocity(), joystick.getAxis())
                joystick.setDirection(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                this.onInteract(ON_RELEASE)
                joystick.draw = false
            }
        }
    }

    override fun draw(canvas: Canvas) {
        if (joystick.draw) {
            joystick.draw(canvas)
        }
    }

    companion object {
        const val ON_DOWN = "onDown"
        const val ON_MOVE = "onMove"
        const val ON_RELEASE = "onRelease"
    }
}