package br.com.mrocigno.gameengine.tools

import br.com.mrocigno.gameengine.base.Ticker
import java.lang.Long.min

class CyclicalTicker(
    private val durationMillis: Long,
    private val onCycleEnd: (() -> Unit)? = null,
    private val onTick: (fraction: Float) -> Boolean
) : Ticker {
    private var initialTime = System.currentTimeMillis()

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