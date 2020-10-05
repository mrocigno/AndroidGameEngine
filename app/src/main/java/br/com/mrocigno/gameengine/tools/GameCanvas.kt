package br.com.mrocigno.gameengine.tools

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GameScene

class GameCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var scene: GameScene? = null

    private val engine get() = context as GameEngine

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            scene?.draw(canvas)
            engine.gamePad?.draw(canvas)
        }
    }
}


