package br.com.mrocigno.gameengine.tools

import android.util.Log

class GameLoop(
    private val update: () -> Unit
) : Thread() {

    init {
        instance = this
    }

    var lastFpsCount = 0

    private var targetTime = 1000 / 60
    private var fps = 0
    private var fps2 = 0

    override fun run() {
        isRunning = true
        var lastUpdate = System.currentTimeMillis()
        var fpsLastTime = System.currentTimeMillis()
        while (isRunning) kotlin.runCatching {
            val now = System.currentTimeMillis()

            fps2++

            val delta = (now - lastUpdate) / targetTime
            if ((now - lastUpdate) >= (targetTime - delta)) {
                fps++
                update.invoke()
                lastUpdate = System.currentTimeMillis()
            }

            if (lastUpdate - fpsLastTime >= 1000) {
                Log.d("GameEngine", "FPS: $fps    loopTimes: $fps2")
                fpsLastTime = System.currentTimeMillis()
                lastFpsCount = fps
                fps = 0
                fps2 = 0
            }
        }
    }

    companion object {
        var instance: GameLoop? = null
        var isRunning = false
    }
}