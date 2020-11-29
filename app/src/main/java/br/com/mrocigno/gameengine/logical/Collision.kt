package br.com.mrocigno.gameengine.logical

import android.util.Log
import br.com.mrocigno.gameengine.base.GameDrawable

interface Collision {
    fun matchBound(bounds: GameBounds) : Boolean
    fun onCollide(hitObject: GameDrawable)
}