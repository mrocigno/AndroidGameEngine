package br.com.mrocigno.gameengine

import android.graphics.*
import android.util.Log
import br.com.mrocigno.gameengine.base.*
import br.com.mrocigno.gameengine.control.DoubleJoystickGamePad
import br.com.mrocigno.gameengine.tools.CyclicalTicker
import br.com.mrocigno.gameengine.tools.SimpleTicker
import br.com.mrocigno.gameengine.utils.getCollisionInsideNewBounds
import br.com.mrocigno.gameengine.utils.getCollisionOutsideNewBounds
import br.com.mrocigno.gameengine.utils.isOutOfWindowBounds
import br.com.mrocigno.gameengine.utils.toDp

class Persona(engine: GameEngine) : GameDrawable(engine), GamePad.OnInteract {
    override val bounds: RectF = RectF(
        500f.toDp(),
        200f.toDp(),
        540f.toDp(),
        260f.toDp()
    )

    private val shoots = mutableListOf<Shoot>()
    private var joystickHelperMove: JoystickHelper? = null
    private var joystickHelperAngle: JoystickHelper? = null
    private val paint = Paint()
    private var cyclicalTickerMove = CyclicalTicker(
        engine,
        20,
        ::move,
        ::infinity
    )
    private var cyclicalTickerShoots = CyclicalTicker(
        engine,
        100,
        ::shoot,
        ::infinity
    )

    override fun onCollide(hitObject: GameDrawable) {
        bounds.getCollisionOutsideNewBounds(hitObject.bounds)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(Path().apply {
            fillType = Path.FillType.EVEN_ODD
            moveTo(bounds.left, bounds.bottom)
            lineTo(bounds.right, bounds.bottom)
            lineTo(bounds.centerX(), bounds.top)
            lineTo(bounds.left, bounds.bottom)
            close()
            this.transform(Matrix().apply {
                this.postRotate(joystickHelperAngle?.getDegrees() ?: 0f, bounds.centerX(), bounds.centerY() + 10f.toDp())
            })
        }, paint)
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
            it.getCollisionInsideNewBounds(engine.getWindowBounds())
            bounds.set(it)
        }
    }

    private fun shoot() {
        joystickHelperAngle?.let {
            val shootBounds = RectF(
                bounds.centerX(),
                bounds.centerY(),
                bounds.centerX() + 10f.toDp(),
                bounds.centerY() + 20f.toDp()
            )
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
    override val bounds: RectF,
    private val joystickHelper: JoystickHelper
) : GameDrawable(engine) {

    private val paint = Paint()

    init {
        val x = bounds.left
        CyclicalTicker(engine, 10, ::move) {
            bounds.isOutOfWindowBounds(engine)
        }.start()
    }

    private fun move() {
        joystickHelper.nextRect(bounds).let {
            bounds.set(it)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(
            Path().apply {
                moveTo(bounds.left, bounds.top)
                lineTo(bounds.left, bounds.bottom)
                lineTo(bounds.right, bounds.bottom)
                lineTo(bounds.right, bounds.top)
                lineTo(bounds.left, bounds.top)
                close()
                transform(Matrix().apply {
                    postRotate(joystickHelper.getDegrees(), bounds.centerX(), bounds.centerY())
                })
            },
            paint
        )
    }
}
