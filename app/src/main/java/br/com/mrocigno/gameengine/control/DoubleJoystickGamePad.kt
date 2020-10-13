package br.com.mrocigno.gameengine.control

import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.base.Joystick

class DoubleJoystickGamePad(
    private val engine: GameEngine
) : GamePad() {

    private var joystickLeft = Joystick()
    private var joystickRight = Joystick()

    override fun onTouchListener(view: View, event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                processPointerDown(event.actionIndex, event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                processPointerDown(event.actionIndex, event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (joystickLeft.hasId) {
                    joystickLeft.setDirection(
                        event.getX(event.findPointerIndex(joystickLeft.joystickId)),
                        event.getY(event.findPointerIndex(joystickLeft.joystickId))
                    )
                    this.onInteract(
                        ON_LEFT_JOYSTICK_MOVE,
                        joystickLeft.getRadian(), joystickLeft.getVelocity(), joystickLeft.getAxis()
                    )
                }
                if (joystickRight.hasId) {
                    joystickRight.setDirection(
                        event.getX(event.findPointerIndex(joystickRight.joystickId)),
                        event.getY(event.findPointerIndex(joystickRight.joystickId))
                    )
                    this.onInteract(
                        ON_RIGHT_JOYSTICK_MOVE,
                        joystickRight.getRadian(), joystickRight.getVelocity(), joystickRight.getAxis()
                    )
                }
            }
            MotionEvent.ACTION_UP -> {
                processPointerUp(event.actionIndex, event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                processPointerUp(event.actionIndex, event)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        if (joystickLeft.draw) joystickLeft.draw(canvas)
        if (joystickRight.draw) joystickRight.draw(canvas)
    }

    private fun processPointerDown(pointerIndex: Int, event: MotionEvent) {
        val (x: Float, y: Float) = event.getX(pointerIndex) to event.getY(pointerIndex)
        if (isOnLeft(x)) {
            this.onInteract(ON_LEFT_JOYSTICK_DOWN)
            joystickLeft.setCenter(x, y)
            joystickLeft.joystickId = event.getPointerId(pointerIndex)
        } else {
            this.onInteract(ON_RIGHT_JOYSTICK_DOWN)
            joystickRight.setCenter(x, y)
            joystickRight.joystickId = event.getPointerId(pointerIndex)
        }
    }

    private fun processPointerUp(pointerIndex: Int, event: MotionEvent) {
        if (joystickLeft.matchIndex(pointerIndex, event)) {
            this.onInteract(ON_LEFT_JOYSTICK_RELEASE)
            joystickLeft.clearId()
        }
        if (joystickRight.matchIndex(pointerIndex, event)) {
            this.onInteract(ON_RIGHT_JOYSTICK_RELEASE)
            joystickRight.clearId()
        }
    }

    private fun isOnLeft(x: Float) = x < (engine.windowBounds!!.right / 2)

    companion object {

        const val ON_LEFT_JOYSTICK_DOWN = "onLeftJoystickDown"
        const val ON_LEFT_JOYSTICK_MOVE = "onLeftJoystickMove"
        const val ON_LEFT_JOYSTICK_RELEASE = "onLeftJoystickRelease"

        const val ON_RIGHT_JOYSTICK_DOWN = "onRightJoystickDown"
        const val ON_RIGHT_JOYSTICK_MOVE = "onRightJoystickMove"
        const val ON_RIGHT_JOYSTICK_RELEASE = "onRightJoystickRelease"

    }
}