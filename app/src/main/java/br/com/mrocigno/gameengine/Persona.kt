package br.com.mrocigno.gameengine

import android.graphics.*
import br.com.mrocigno.gameengine.base.*
import br.com.mrocigno.gameengine.control.DoubleJoystickGamePad
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.animation.CyclicalAnimationController
import br.com.mrocigno.gameengine.utils.isOutOfWindowBounds
import br.com.mrocigno.gameengine.utils.toDp

class Persona(engine: GameEngine) : GameDrawable(engine), GamePad.OnInteract {
    override val bounds = GameBounds(
        0f.toDp(),
        0f.toDp(),
        40f.toDp(),
        60f.toDp()
    ).apply {
        setCenter(1260f, 540f)
    }

    private val shoots = mutableListOf<Shoot>()
    private var joystickHelperMove: JoystickHelper? = null
    private var joystickHelperAngle: JoystickHelper? = null
    private val paint = Paint()
    private var cyclicalTickerMove = CyclicalAnimationController(
        engine,
        20,
        ::move,
        ::infinity
    )
    private var cyclicalTickerShoots = CyclicalAnimationController(
        engine,
        100,
        ::shoot,
        ::infinity
    )
    private val persona = Path()
        get() = field.apply {
            reset()
            fillType = Path.FillType.EVEN_ODD
            moveTo(bounds.left, bounds.bottom)
            lineTo(bounds.right, bounds.bottom)
            lineTo(bounds.centerX(), bounds.top)
            lineTo(bounds.left, bounds.bottom)
            close()
            bounds.rotate(joystickHelperAngle?.getDegrees() ?: 0f, bounds.width()/2, bounds.height()/2 + 10f.toDp())
            transform(bounds.rotationMatrix)
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(persona, paint)
        synchronized(shoots) {
            shoots.removeIf {
                it.draw(canvas)
                it.bounds.isOutOfWindowBounds(engine)
            }
        }
    }

    override fun toString(): String {
        return "Persona"
    }

    private fun move() {
        joystickHelperMove?.nextRect(bounds)?.let {
            bounds.set(it)
        }
    }

    private fun shoot() {
        joystickHelperAngle?.let {
            val shootBounds = GameBounds(
                bounds.centerX(),
                bounds.centerY(),
                bounds.centerX() + 10.toDp(),
                bounds.centerY() + 20.toDp()
            )
            shootBounds.rotate(it.getDegrees(), 5.toDp(), 10.toDp())
            synchronized(shoots) {
                shoots.add(Shoot(engine, shootBounds, it))
            }
        }
    }

    private fun infinity(fraction: Float): Boolean {
        return false
    }

    //region OnInteract implements
    override fun onInteract(
        interactionType: String,
        joystickHelper: JoystickHelper?
    ) {
        when (interactionType) {
            DoubleJoystickGamePad.ON_LEFT_JOYSTICK_DOWN -> onDown()
            DoubleJoystickGamePad.ON_LEFT_JOYSTICK_MOVE -> onMove(joystickHelper!!)
            DoubleJoystickGamePad.ON_LEFT_JOYSTICK_RELEASE -> onRelease()
            DoubleJoystickGamePad.ON_RIGHT_JOYSTICK_DOWN -> onAngleDown()
            DoubleJoystickGamePad.ON_RIGHT_JOYSTICK_MOVE -> onAngleMove(joystickHelper!!)
            DoubleJoystickGamePad.ON_RIGHT_JOYSTICK_RELEASE -> onAngleRelease()
        }
    }

    private fun onAngleDown() {
        cyclicalTickerShoots.start()
    }

    private fun onAngleMove(joystickHelper: JoystickHelper) {
        if (joystickHelper.velocity < .1) return
        joystickHelperAngle = joystickHelper
        joystickHelperAngle?.distance = 15f
    }

    private fun onAngleRelease() {
        cyclicalTickerShoots.stop()
    }

    private fun onDown() {
        cyclicalTickerMove.start()
    }

    private fun onMove(joystickHelper: JoystickHelper) {
        joystickHelperMove = joystickHelper
    }

    private fun onRelease() {
        cyclicalTickerMove.stop()
    }
    //endregion
}

class Shoot(
    engine: GameEngine,
    override val bounds: GameBounds,
    private val joystickHelper: JoystickHelper
) : GameDrawable(engine) {

    private val paint = Paint()

    init {
        CyclicalAnimationController(engine, 10, ::move) {
            bounds.isOutOfWindowBounds(engine)
        }.start()
    }

    private fun move() {
        joystickHelper.nextRect(bounds).let {
            bounds.set(it)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(
            bounds.path,
            paint
        )
    }
}
