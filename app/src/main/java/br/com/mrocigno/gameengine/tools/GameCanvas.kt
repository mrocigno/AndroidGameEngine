package br.com.mrocigno.gameengine.tools

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine

class GameCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val drawables = mutableListOf<GameDrawable>()
    private val engine
        get() = context as GameEngine

    init {
        engine.gamePad?.apply {
            setOnTouchListener { v, event ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.performClick()
                        this.setCardinals(event.x, event.y)
                        drawables.add(this)
                    }
                    MotionEvent.ACTION_UP -> {
                        this.onRelease()
                        drawables.remove(this)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        this.onMove()
                        this.setDirection(event.x, event.y)
                    }
                }
                true
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawables.forEach { e ->
                e.draw(it)
            }
        }
    }
}


