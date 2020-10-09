package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import br.com.mrocigno.gameengine.base.*
import br.com.mrocigno.gameengine.control.DefaultGamePad
import br.com.mrocigno.gameengine.tools.CyclicalTicker
import br.com.mrocigno.gameengine.utils.getCollisionInsideNewBounds
import br.com.mrocigno.gameengine.utils.getCollisionOutsideNewBounds
import br.com.mrocigno.gameengine.utils.toDp
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

class Persona(engine: GameEngine) : GameDrawable(engine), GamePad.OnInteract {

    override var positionX = 500f.toDp()
    override var positionY = 200f.toDp()
    override var width = 20f.toDp()
    override var height = 20f.toDp()

    private var acresX = 0f
    private var acresY = 0f
    private var axis: GamePadAxis = GamePadAxis.NORTHWEST
    private val paint = Paint()
    private var cyclicalTicker = CyclicalTicker(
        engine,
        10,
        ::onCycleEnd,
        ::move
    )

    override fun onCollide(hitObject: GameDrawable) {
        val personaBounds = getBounds().getCollisionOutsideNewBounds(hitObject.getBounds())
        positionX = personaBounds.centerX()
        positionY = personaBounds.centerY()
    }

    override fun getBounds() = RectF(
        positionX - width,
        positionY - width,
        positionX + width,
        positionY + width
    )

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(positionX, positionY, width, paint)
    }

    override fun toString(): String {
        return "Persona"
    }

    private fun onCycleEnd() {
        val nextRect = when (axis) {
            GamePadAxis.NORTHEAST -> {
                getNextPosition(positionX + acresX, positionY - acresY)
            }
            GamePadAxis.SOUTHEAST -> {
                getNextPosition(positionX + acresX, positionY + acresY)
            }
            GamePadAxis.NORTHWEST -> {
                getNextPosition(positionX - acresX, positionY - acresY)
            }
            GamePadAxis.SOUTHWEST -> {
                getNextPosition(positionX - acresX, positionY + acresY)
            }
        }
        nextRect.getCollisionInsideNewBounds(engine.getWindowBounds())
        positionX = nextRect.centerX()
        positionY = nextRect.centerY()
    }

    private fun getNextPosition(nextX: Float, nextY: Float) = RectF(
        nextX - width, nextY - height, nextX + width, nextY + height
    )

    private fun move(fraction: Float): Boolean {
        return false
    }

    //region OnInteract implements
    override fun onInteract(
        interactionType: String,
        radian: Float?,
        velocity: Float?,
        axis: GamePadAxis?
    ) {
        when (interactionType) {
            DefaultGamePad.ON_DOWN -> onDown()
            DefaultGamePad.ON_MOVE -> onMove(radian!!, velocity!!, axis!!)
            DefaultGamePad.ON_RELEASE -> onRelease()
        }
    }

    private fun onMove(radian: Float, velocity: Float, axis: GamePadAxis) {
        val distance = 15f * velocity
        this.acresX = distance * cos(radian)
        this.acresY = sqrt(distance.pow(2) - acresX.pow(2))
        this.axis = axis
    }

    private fun onDown() {
        cyclicalTicker.start()
    }

    private fun onRelease() {
        acresX = 0f
        acresY = 0f
        cyclicalTicker.stop()
    }
    //endregion
}