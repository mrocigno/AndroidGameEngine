package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GameScene
import br.com.mrocigno.gameengine.control.DoubleJoystickGamePad
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.utils.toDp

class MainActivity : GameEngine() {

    override val gamePad = DoubleJoystickGamePad(this)
    override fun initialScene() = TestScene(this)

    override val debugMode: Boolean = true
    override val showFpsCount: Boolean = true
}

class TestScene(engine: GameEngine) : GameScene(engine) {

    private val persona = Persona(engine).apply {
        setOnClickListener {
            camera.follow = this
        }
    }

    private val aaa = Square(
        engine = engine,
        x = 800f,
        color = Color.RED
    ).apply {
        setOnClickListener {
            this.bounds.scale = 1.5f
        }
    }

    override fun onCreate() {
        super.onCreate()
        camera.follow = persona
        camera.moveTo(persona.bounds)
    }

    override fun getComponents() = setOf(
        TestFloor(engine),
        persona,
        Square(engine, color = Color.BLUE).apply {
            setOnClickListener {
                aaa.bounds.scale = (.5f)
            }
        },
        aaa
    )
}

class TestFloor(engine: GameEngine) : GameDrawable(engine) {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.toDp()
    }

    override val bounds = GameBounds(
        0f, 0f, 1000f, 1000f
    )

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(bounds.path, paint)
    }
}