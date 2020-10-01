package br.com.mrocigno.gameengine.base

import android.os.Bundle
import android.view.WindowManager
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
                game_canvas.drawables.add(this)
                gamePad?.gamePadObservers?.addAll(getControllable(this.components))
            }
        }

    private fun getControllable(components: List<GameDrawable>): Collection<GamePad.OnMove> =
        components.filter { it is GamePad.OnMove }.map { it as GamePad.OnMove }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        GameLoop(::onUpdate).start()
        scene = initialScene
    }

    open fun onUpdate() {
        game_canvas.invalidate()
    }
}