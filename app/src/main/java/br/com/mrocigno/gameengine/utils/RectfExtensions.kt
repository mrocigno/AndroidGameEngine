package br.com.mrocigno.gameengine.utils

import android.graphics.RectF
import android.util.Log
import androidx.core.graphics.toRect
import br.com.mrocigno.gameengine.base.GameEngine
import br.com.mrocigno.gameengine.logical.GameBounds
import kotlin.math.abs

infix fun GameBounds.avoidCollisionWith(comparison: RectF) {
    val left = abs(comparison.left - this.right)
    val top = abs(comparison.top - this.bottom)
    val right = abs(comparison.right - this.left)
    val bottom = abs(comparison.bottom - this.top)

    if (left < top && left < right && left < bottom) {
        set(
            this.left - left,
            this.top,
            this.right - left,
            this.bottom,
            false
        )
    } else if (top < left && top < right && top < bottom) {
        set(
            this.left,
            this.top - top,
            this.right,
            this.bottom - top,
            false
        )
    } else if (right < left && right < top && right < bottom) {
        set(
            this.left + right,
            this.top,
            this.right + right,
            this.bottom,
            false
        )
    } else if (bottom < left && bottom < top && bottom < right) {
        set(
            this.left,
            this.top + bottom,
            this.right,
            this.bottom + bottom,
            false
        )
    }
}

infix fun GameBounds.stayInside(insideOf: RectF): RectF {
    var left = this.left
    var top = this.top
    var right = this.right
    var bottom = this.bottom

    if (this.left < insideOf.left) {
        right += abs(left - insideOf.left)
        left = insideOf.left
    }
    if (this.top < insideOf.top) {
        bottom += abs(top - insideOf.top)
        top = insideOf.top
    }
    if (this.right > insideOf.right) {
        left -= abs(right - insideOf.right)
        right = insideOf.right
    }
    if (this.bottom > insideOf.bottom) {
        top -= abs(bottom - insideOf.bottom)
        bottom = insideOf.bottom
    }

    set(left, top, right, bottom, false)
    return this
}

fun RectF.isOutOfWindowBounds(engine: GameEngine): Boolean =
    engine.scene?.camera?.renderBounds?.intersects(left, top, right, bottom) == false