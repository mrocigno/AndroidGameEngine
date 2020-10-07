package br.com.mrocigno.gameengine.base

abstract class GameAnimationController(
    private val engine: GameEngine,
    val durationMillis: Long
) {
    protected var initialTime = System.currentTimeMillis()
    abstract fun handle(): Boolean

    fun start() {
        engine.addTicker(this)
    }

    fun stop() {
        engine.removeTicker(this)
    }
}