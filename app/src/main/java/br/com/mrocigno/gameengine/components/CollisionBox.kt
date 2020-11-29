package br.com.mrocigno.gameengine.components

import android.util.Log
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.logical.Collision
import br.com.mrocigno.gameengine.logical.GameBounds

abstract class CollisionBox(engine: GameEngine) : GameDrawable(engine), Collision {

    override fun matchBound(bounds: GameBounds) = this.bounds.intersects(bounds)

    override fun onCollide(hitObject: GameDrawable) {
        Log.d("GameEngine", "$this collide with: $hitObject")
    }
}