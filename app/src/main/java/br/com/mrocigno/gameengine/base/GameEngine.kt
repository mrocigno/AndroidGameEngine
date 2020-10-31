package br.com.mrocigno.gameengine.base

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import br.com.mrocigno.gameengine.tools.GameLoop
import br.com.mrocigno.gameengine.R
import br.com.mrocigno.gameengine.logical.GameBounds
import kotlinx.android.synthetic.main.activity_game.*

abstract class GameEngine : AppCompatActivity(R.layout.activity_game) {

    abstract val gamePad: GamePad?
    abstract val initialScene: GameScene
    open val debugMode = false
    open val showFpsCount = false
    val lastFpsCount get() = loop.lastFpsCount
    var windowBounds: GameBounds? = null
    var scene: GameScene? = null
        set(value) {
            field = value?.apply {
                value.onCreate()
                game_canvas.scene = value
            }
        }

    private val tickerList = mutableSetOf<GameAnimationController>()
    private val loop = GameLoop(
        ::onUpdate,
        ::onLoop
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(FLAG_KEEP_SCREEN_ON)
        loop.start()
    }

    fun addTicker(ticker: GameAnimationController) = tickerList.add(ticker)
    fun removeTicker(ticker: GameAnimationController) = tickerList.remove(ticker)

    fun onMeasured(width: Int, height: Int) {
        windowBounds = GameBounds(0f, 0f, width.toFloat(), height.toFloat())
        scene = initialScene
    }

    private fun onUpdate() {
        game_canvas.invalidate()

    }

    private fun onLoop() {
        Log.d("A mano", "=======")
        tickerList.removeIf { it.handle() }
        Log.d("A mano", "*******")
    }
}