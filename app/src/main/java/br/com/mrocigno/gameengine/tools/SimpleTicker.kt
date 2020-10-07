package br.com.mrocigno.gameengine.tools

import br.com.mrocigno.gameengine.base.GameAnimationController
import br.com.mrocigno.gameengine.base.GameEngine
import java.lang.Long.min

class SimpleTicker(
    engine: GameEngine,
    durationMillis: Long,
    private val onTick: (fraction: Float) -> Unit
) : GameAnimationController(engine, durationMillis) {

    override fun handle() : Boolean {
        val current = System.currentTimeMillis() - initialTime
        val fraction = min((current * 100) / durationMillis, 100) / 100f
        onTick.invoke(fraction)
        return fraction == 1f
    }
}