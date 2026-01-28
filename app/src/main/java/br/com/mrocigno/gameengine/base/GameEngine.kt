package br.com.mrocigno.gameengine.base

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import br.com.mrocigno.gameengine.R
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.tools.GameCanvas
import br.com.mrocigno.gameengine.tools.GameLoop

abstract class GameEngine : AppCompatActivity(R.layout.activity_game) {

    abstract val gamePad: GamePad?
    abstract fun initialScene(): GameScene
    open val debugMode = false
    open val showFpsCount = false
    val gameCanvas: GameCanvas by lazy {
        findViewById<GameCanvas>(R.id.game_canvas)
    }
    val lastFpsCount get() = loop.lastFpsCount
    var windowBounds: GameBounds? = null
    var scene: GameScene? = null
        set(value) {
            field = value?.apply {
                value.onCreate()
                gameCanvas.scene = value
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
        scene = initialScene()
    }

    private fun onUpdate() {
        gameCanvas.invalidate()
    }

    private fun onLoop() {
        tickerList.removeIf { it.handle() }
    }
}