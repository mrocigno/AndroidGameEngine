package br.com.mrocigno.gameengine

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.base.GameScene
import br.com.mrocigno.gameengine.control.DefaultGamePad

class MainActivity : GameEngine() {

    override val gamePad = DefaultGamePad()

    override val initialScene = TestScene(this)

}

class TestScene(engine: GameEngine) : GameScene(engine) {

    override val components = listOf(
        Persona(engine),
        Square(engine = engine, color = Color.BLUE).apply {
            setOnClickListener {
                this.expandAnimation()
            }
        },
        Square(engine = engine, x = 400f, color = Color.RED).apply {
            setOnClickListener {
                this.expandAnimation()
            }
        }
    )

    override fun draw(canvas: Canvas) {
        components.forEach { it.draw(canvas) }
    }
}
