package br.com.mrocigno.gameengine.animation

import android.graphics.PointF
import android.util.Log
import br.com.mrocigno.gameengine.base.GameAnimationController
import br.com.mrocigno.gameengine.base.GameEngine
import java.lang.Long.min

class SimpleAnimationController(
    engine: GameEngine,
    durationMillis: Long,
    onUpdate: ((fraction: Float) -> Unit)? = null
) : GameAnimationController(engine, durationMillis) {

    init {
        onUpdate?.let { addUpdateListener(it) }
    }

    override fun handle() : Boolean {
        val current = System.currentTimeMillis() - initialTime
        val fraction = min((current * 100) / durationMillis, 100) / 100f
        observers.forEach { it.invoke(fraction) }
        return fraction == 1f
    }
}

