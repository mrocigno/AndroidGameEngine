package br.com.mrocigno.gameengine.tools

import br.com.mrocigno.gameengine.base.GameAnimationController
import br.com.mrocigno.gameengine.base.GameEngine
import java.lang.Long.min

class CyclicalTicker(
    engine: GameEngine,
    durationMillis: Long,
    private val onCycleEnd: (() -> Unit)? = null,
    private val onTick: (fraction: Float) -> Boolean
) : GameAnimationController(engine, durationMillis) {

    override fun handle() : Boolean {
        val current = System.currentTimeMillis() - initialTime
        val fraction = min((current * 100) / durationMillis, 100) / 100f
        val result = onTick.invoke(fraction)
        if (fraction == 1f) {
            onCycleEnd?.invoke()
            initialTime = System.currentTimeMillis()
        }
        return result
    }
}