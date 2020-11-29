package br.com.mrocigno.gameengine.components

import android.util.Log
import androidx.annotation.CallSuper
import br.com.mrocigno.gameengine.base.GameDrawable
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.logical.Collision
import br.com.mrocigno.gameengine.logical.GameBounds

abstract class BruisedBox(engine: GameEngine) : GameDrawable(engine), Collision {

    abstract var life: Int

    abstract fun whenGettingHurt(power: Int)

    override fun matchBound(bounds: GameBounds) = this.bounds.intersects(bounds)

    @CallSuper
    override fun onCollide(hitObject: GameDrawable) {
        if (hitObject is HurtBox) {
            whenGettingHurt(hitObject.power)
        }
    }
}