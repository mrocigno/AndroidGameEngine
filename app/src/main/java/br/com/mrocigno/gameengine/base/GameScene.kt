package br.com.mrocigno.gameengine.base

abstract class GameScene : GameDrawable() {

    abstract val components: List<GameDrawable>

}