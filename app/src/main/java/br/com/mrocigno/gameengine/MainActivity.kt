package br.com.mrocigno.gameengine

import android.graphics.Color
import br.com.mrocigno.gameengine.base.*
import br.com.mrocigno.gameengine.control.DoubleJoystickGamePad

class MainActivity : GameEngine() {

    override val gamePad = DoubleJoystickGamePad(this)

    override val initialScene = TestScene(this)

    override val debugMode: Boolean = true
    override val showFpsCount: Boolean = true

}

class TestScene(engine: GameEngine) : GameScene(engine) {

    private val persona = Persona(engine).apply {
        setOnClickListener {
            camera.follow = this
        }
    }

    override fun onCreate() {
        super.onCreate()
        camera.follow = persona
    }

    override fun getComponents() = listOf(
        persona,
        Square(engine = engine, color = Color.BLUE).apply {
            setOnClickListener {
                camera.moveTo(this.bounds.centerX(), this.bounds.centerY())
            }
        },
        Square(engine = engine, x = 800f, color = Color.RED).apply {
            setOnClickListener {
                this.expandAnimation()
            }
        }
    )
}
