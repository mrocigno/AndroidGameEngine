package br.com.mrocigno.gameengine.animation

import br.com.mrocigno.gameengine.base.GameAnimationController
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.base.GameScene
import java.lang.Long.min

class CyclicalAnimationController(
    engine: GameEngine,
    durationMillis: Long,
    private val onCycleEnd: (() -> Unit)? = null,
    private val onUpdate: (fraction: Float) -> Boolean
) : GameAnimationController(engine, durationMillis) {

    override fun handle() : Boolean {
        val current = System.currentTimeMillis() - initialTime
        val fraction = min((current * 100) / durationMillis, 100) / 100f
        val result = onUpdate.invoke(fraction)
        if (fraction == 1f) {
            onCycleEnd?.invoke()
            initialTime = System.currentTimeMillis()
        }
        return result
    }
}