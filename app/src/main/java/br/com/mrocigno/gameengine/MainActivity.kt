package br.com.mrocigno.gameengine

import android.graphics.Color
import br.com.mrocigno.gameengine.base.*
import br.com.mrocigno.gameengine.control.DoubleJoystickGamePad

class MainActivity : GameEngine() {

    override val gamePad = DoubleJoystickGamePad(this)
    override fun initialScene() = TestScene(this)

    override val debugMode: Boolean = false
    override val showFpsCount: Boolean = true

}

class TestScene(engine: GameEngine) : GameScene(engine) {

    private val persona = Persona(engine).apply {
        setOnClickListener {
            camera.follow = this
        }
    }

    private val aaa = Square(
        engine, x = 800f,
        color = Color.RED
    ).apply {
        setOnClickListener {
            this.bounds.scale = (1.5f)
        }
    }

    override fun onCreate() {
        super.onCreate()
        camera.follow = persona
    }

    override fun getComponents() = mutableSetOf(
        persona,
        Square(engine, color = Color.BLUE).apply {
            setOnClickListener {
                aaa.bounds.scale = (.5f)
            }
        },
        aaa
    )
}
