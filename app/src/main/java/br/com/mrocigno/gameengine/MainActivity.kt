package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.util.Log
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.base.GameScene
import br.com.mrocigno.gameengine.control.DefaultGamePad

class MainActivity : GameEngine() {

    override val gamePad = DefaultGamePad()

    override val initialScene = TestScene()

}

class TestScene : GameScene() {

    override val components = listOf(
        Persona(),
        Square().apply {
            setOnClickListener {
                this.expandAnimation()
            }
        }
    )

    override fun draw(canvas: Canvas) {
        components.forEach { it.draw(canvas) }
    }
}
