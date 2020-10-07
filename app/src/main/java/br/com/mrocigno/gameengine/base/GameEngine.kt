package br.com.mrocigno.gameengine.base

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
                game_canvas.scene = value
                gamePad?.gamePadObservers?.addAll(getControllable(components))
            }
        }
    private val tickerList = mutableListOf<GameAnimationController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        GameLoop(::onUpdate).start()
        scene = initialScene
    }

    fun addTicker(ticker: GameAnimationController) = tickerList.add(ticker)
    fun removeTicker(ticker: GameAnimationController) = tickerList.remove(ticker)

    open fun onUpdate() {
        tickerList.removeIf { it.handle() }
        game_canvas.invalidate()
    }

    private fun getControllable(components: List<GameDrawable>): Collection<GamePad.OnMove> =
        components.filterIsInstance(GamePad.OnMove::class.java)
}