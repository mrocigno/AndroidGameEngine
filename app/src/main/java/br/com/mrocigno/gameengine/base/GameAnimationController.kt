package br.com.mrocigno.gameengine.base

abstract class GameAnimationController(
    private val engine: GameEngine,
    val durationMillis: Long
) {

    var isRunning = false
    protected val observers = mutableSetOf<(fraction: Float) -> Unit>()
    protected var initialTime: Long = 0
    abstract fun handle(): Boolean

    fun addUpdateListener(onUpdate: (fraction: Float) -> Unit) {
        observers.add(onUpdate)
    }

    fun removeUpdateListener(onUpdate: (fraction: Float) -> Unit) {
        observers.remove(onUpdate)
    }

    fun start() {
        initialTime = System.currentTimeMillis()
        engine.addTicker(this)
        isRunning = true
    }

    fun stop() {
        engine.removeTicker(this)
    }
}