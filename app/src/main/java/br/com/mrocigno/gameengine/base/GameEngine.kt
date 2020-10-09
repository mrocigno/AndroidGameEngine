package br.com.mrocigno.gameengine.base

import android.graphics.RectF
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import br.com.mrocigno.gameengine.tools.GameLoop
import br.com.mrocigno.gameengine.R
import kotlinx.android.synthetic.main.activity_game.*

abstract class GameEngine : AppCompatActivity(R.layout.activity_game) {

    abstract val gamePad: GamePad?
    abstract val initialScene: GameScene
    var scene: GameScene? = null
        set(value) {
            field = value?.apply {
                value.onCreate()
                game_canvas.scene = value
            }
        }
    private val tickerList = mutableListOf<GameAnimationController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        GameLoop(
            ::onUpdate,
            ::onLoop
        ).start()
        scene = initialScene
    }

    fun addTicker(ticker: GameAnimationController) = tickerList.add(ticker)
    fun removeTicker(ticker: GameAnimationController) = tickerList.remove(ticker)

    fun getWindowBounds() = RectF(
        0f, 0f, game_canvas.width.toFloat(), game_canvas.height.toFloat()
    )

    private fun onUpdate() {
        game_canvas.invalidate()
    }

    private fun onLoop() {
        tickerList.removeIf { it.handle() }
    }
}