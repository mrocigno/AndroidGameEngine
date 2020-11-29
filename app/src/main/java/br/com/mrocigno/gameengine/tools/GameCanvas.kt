package br.com.mrocigno.gameengine.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GameScene
import br.com.mrocigno.gameengine.utils.toDp

class GameCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var scene: GameScene? = null

    private val engine get() = context as GameEngine
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 15.toDp()
    }

    init {
        setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.performClick()
                scene?.onTouch(event)
            }
            engine.gamePad?.onTouchListener(v, event)
            true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        engine.onMeasured(right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            scene?.draw(canvas)
            engine.gamePad?.draw(canvas)
            if (engine.showFpsCount)
                canvas.drawText("${engine.lastFpsCount}", 30.toDp(), 30.toDp(), paint)
        }
    }
}


