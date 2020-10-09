package br.com.mrocigno.gameengine.control

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.utils.toDp
import kotlin.math.pow
import kotlin.math.sqrt

class DoubleJoystickGamePad(
    private val engine: GameEngine
) : GamePad() {

    private var joyLeftCenterX = 0f
    private var joyLeftCenterY = 0f
    private var joyLeftDirectionX = 0f
    private var joyLeftDirectionY = 0f
    private var joyLeftPointerId = -1

    private var joyRightCenterX = 0f
    private var joyRightCenterY = 0f
    private var joyRightDirectionX = 0f
    private var joyRightDirectionY = 0f
    private var joyRightPointerId = -1

    private val dpadRadius = 70f.toDp()
    private val directionRadius = 25f.toDp()

    private val filled = Paint()
    private val stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.toDp()
    }

    override fun onTouchListener(view: View, event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                processPointerDown(event.actionIndex, event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                processPointerDown(event.actionIndex, event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (joyLeftPointerId != -1) {
                    setDirectionLeft(
                        event.getX(event.findPointerIndex(joyLeftPointerId)),
                        event.getY(event.findPointerIndex(joyLeftPointerId))
                    )
                }
                if (joyRightPointerId != -1) {
                    setDirectionRight(
                        event.getX(event.findPointerIndex(joyRightPointerId)),
                        event.getY(event.findPointerIndex(joyRightPointerId))
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
        if (joyLeftPointerId != -1) {
            canvas.drawCircle(
                joyLeftCenterX, joyLeftCenterY, dpadRadius, stroke
            )

            canvas.drawCircle(
                joyLeftDirectionX, joyLeftDirectionY, directionRadius, filled
            )
        }
        if (joyRightPointerId != -1) {
            canvas.drawCircle(
                joyRightCenterX, joyRightCenterY, dpadRadius, stroke
            )

            canvas.drawCircle(
                joyRightDirectionX, joyRightDirectionY, directionRadius, filled
            )
        }
    }

    private fun setJoystickLeftCenter(centerX: Float, centerY: Float) {
        joyLeftCenterX = centerX
        joyLeftCenterY = centerY
    }

    private fun setDirectionLeft(directionX: Float, directionY: Float) {
        val deltaX = (joyLeftCenterX - directionX).pow(2)
        val deltaY = (joyLeftCenterY - directionY).pow(2)
        val distance = sqrt(deltaX + deltaY)

        var newX = directionX
        var newY = directionY
        if (distance >= dpadRadius) {
            newX = joyLeftCenterX + (directionX - joyLeftCenterX) * (dpadRadius / distance)
            newY = joyLeftCenterY + (directionY - joyLeftCenterY) * (dpadRadius / distance)
        }

        this.joyLeftDirectionX = newX
        this.joyLeftDirectionY = newY
    }

    private fun setDirectionRight(directionX: Float, directionY: Float) {
        val deltaX = (joyRightCenterX - directionX).pow(2)
        val deltaY = (joyRightCenterY - directionY).pow(2)
        val distance = sqrt(deltaX + deltaY)

        var newX = directionX
        var newY = directionY
        if (distance >= dpadRadius) {
            newX = joyRightCenterX + (directionX - joyRightCenterX) * (dpadRadius / distance)
            newY = joyRightCenterY + (directionY - joyRightCenterY) * (dpadRadius / distance)
        }

        this.joyRightDirectionX = newX
        this.joyRightDirectionY = newY
    }

    private fun setJoystickRightCenter(centerX: Float, centerY: Float) {
        joyRightCenterX = centerX
        joyRightCenterY = centerY
    }

    private fun processPointerDown(pointerIndex: Int, event: MotionEvent) {
        val (x: Float, y: Float) = event.getX(pointerIndex) to event.getY(pointerIndex)
        if (isOnLeft(x)) {
            setJoystickLeftCenter(x, y)
            joyLeftPointerId = event.getPointerId(pointerIndex)
        } else {
            setJoystickRightCenter(x, y)
            joyRightPointerId = event.getPointerId(pointerIndex)
        }
    }

    private fun processPointerUp(pointerIndex: Int, event: MotionEvent) {
        if (event.findPointerIndex(joyLeftPointerId) == pointerIndex) {
            joyLeftPointerId = -1
        } else {
            joyRightPointerId = -1
        }
    }

    private fun isOnLeft(x: Float) = x < (engine.getWindowBounds().right / 2)
}