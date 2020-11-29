package br.com.mrocigno.gameengine.components

import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.logical.Collision
import br.com.mrocigno.gameengine.logical.GameBounds
import br.com.mrocigno.gameengine.utils.getCollisionInsideNewBounds

abstract class Floor(engine: GameEngine) : GameDrawable(engine), Collision {

    override fun matchBound(bounds: GameBounds) = this.bounds.intersects(bounds)

    override fun onCollide(hitObject: GameDrawable) {
        hitObject.bounds.getCollisionInsideNewBounds(this.bounds)
    }
}