package br.com.mrocigno.gameengine.tools

import br.com.mrocigno.gameengine.base.Ticker
import java.lang.Long.min

class SimpleTicker(
    private val durationMillis: Long,
    private val onTick: (fraction: Float) -> Unit
) : Ticker {
    private val initialTime = System.currentTimeMillis()

    override fun handle() : Boolean {
        val current = System.currentTimeMillis() - initialTime
        val fraction = min((current * 100) / durationMillis, 100) / 100f
        onTick.invoke(fraction)
        return fraction == 1f
    }
}