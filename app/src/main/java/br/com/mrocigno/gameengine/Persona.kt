package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import br.com.mrocigno.gameengine.base.*
import br.com.mrocigno.gameengine.tools.CyclicalTicker
import br.com.mrocigno.gameengine.tools.GameLoop
import br.com.mrocigno.gameengine.utils.toDp
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sqrt

class Persona(engine: GameEngine) : GameDrawable(engine), GamePad.OnMove {

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

    override fun onMove(radian: Float, velocity: Float, axis: GamePadAxis) {
        val distance = 10f * velocity
        this.acresX = distance * cos(radian)
        this.acresY = sqrt(distance.pow(2) - acresX.pow(2))
        this.axis = axis
    }

    private fun onCycleEnd() {
        when (axis) {
            GamePadAxis.NORTHEAST -> {
                positionX += acresX
                positionY -= acresY
            }
            GamePadAxis.SOUTHEAST -> {
                positionX += acresX
                positionY += acresY
            }
            GamePadAxis.NORTHWEST -> {
                positionX -= acresX
                positionY -= acresY
            }
            GamePadAxis.SOUTHWEST -> {
                positionX -= acresX
                positionY += acresY
            }
        }
    }

    override fun onDown() {
        cyclicalTicker.start()
    }

    private fun move(fraction: Float) : Boolean {
        return false
    }

    override fun onRelease() {
        acresX = 0f
        acresY = 0f
        cyclicalTicker.stop()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(positionX, positionY, 20f.toDp(), paint)
    }
}