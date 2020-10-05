package br.com.mrocigno.gameengine.tools

import android.util.Log
import br.com.mrocigno.gameengine.base.Ticker

class GameLoop(
    private val update: () -> Unit
) : Thread() {

    init {
        instance = this
    }

    var lastFpsCount = 0

    private var targetTime = 1000 / 60
    private var fps = 0
    private val tickerList = mutableListOf<Ticker>()

    override fun run() {
        isRunning = true
        var lastUpdate = System.currentTimeMillis()
        var fpsLastTime = System.currentTimeMillis()
        while (isRunning) kotlin.runCatching {
            fps++
            val now = System.currentTimeMillis()

            tickerList.removeIf { it.handle() }
            update.invoke()

            val delta = (now - lastUpdate) / targetTime
            lastUpdate = System.currentTimeMillis()

            if (lastUpdate - fpsLastTime >= 1000) {
                Log.d("GameEngine", "FPS: $fps")
                fpsLastTime = System.currentTimeMillis()
                lastFpsCount = fps
                fps = 0
            }

            sleep(targetTime - delta)
        }
    }

    fun addTicker(ticker: Ticker) = tickerList.add(ticker)
    fun removeTicker(ticker: Ticker) = tickerList.remove(ticker)

    companion object {
        var instance: GameLoop? = null
        var isRunning = false
    }
}