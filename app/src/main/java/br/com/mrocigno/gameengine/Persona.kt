package br.com.mrocigno.gameengine

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GamePad
import br.com.mrocigno.gameengine.base.GamePadAxis
import br.com.mrocigno.gameengine.base.Ticker
import br.com.mrocigno.gameengine.tools.CyclicalTicker
import br.com.mrocigno.gameengine.tools.GameLoop
import br.com.mrocigno.gameengine.utils.toDp
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sqrt

class Persona : GameDrawable(), GamePad.OnMove {

    var positionX = 500f.toDp()
    var positionY = 200f.toDp()
    var acresX = 0f
    var acresY = 0f

    var axis: GamePadAxis = GamePadAxis.NORTHWEST

    val paint = Paint()

    override fun onMove(radian: Float, velocity: Float, axis: GamePadAxis) {
        val distance = 15f * velocity
        this.acresX = distance * cos(radian)
        this.acresY = sqrt(distance.pow(2) - acresX.pow(2))
        this.axis = axis

        registerTick(velocity)
    }

    var lastCyclicalTicker: Ticker? = null

    private fun registerTick(velocity: Float) {
        GameLoop.instance?.apply {
            onCycleEnd()
            lastCyclicalTicker?.let { removeTicker(it) }
            lastCyclicalTicker = CyclicalTicker(
                10,
                ::onCycleEnd,
                ::move
            )
            addTicker(lastCyclicalTicker!!)
        }
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

    private fun move(fraction: Float) : Boolean {
        return acresX == 0f && acresY == 0f
    }

    override fun onRelease() {
        acresX = 0f
        acresY = 0f
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(positionX, positionY, 20f.toDp(), paint)
        canvas.drawLine(520f.toDp() + 300f, 150f.toDp(), 520f.toDp() + 300f, 250f.toDp(), paint)
    }
}