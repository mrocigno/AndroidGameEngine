package br.com.mrocigno.gameengine.logical

import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import androidx.core.graphics.rotationMatrix
import androidx.core.graphics.toRectF

class GameBounds(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float
) : RectF(left, top, right, bottom), Cloneable {

    val rotationMatrix = rotationMatrix(0f, 0f, 0f)
    val globalPosition: RectF
        get() = RectF(left - offsetX, top - offsetY, right - offsetX, bottom - offsetY)
    var rotationDegrees = 0f
        private set
    var offsetX = 0f
        private set
    var offsetY = 0f
        private set
    var insetX = 0f
        private set
    var insetY = 0f
        private set
    val path = Path()
        get() = field.apply {
            this.reset()
            moveTo(northwestPoint.x, northwestPoint.y)
            lineTo(northeastPoint.x, northeastPoint.y)
            lineTo(southeastPoint.x, southeastPoint.y)
            lineTo(southwestPoint.x, southwestPoint.y)
            close()
        }

    private val observers = mutableSetOf<OnBoundsChange>()
    private val northwestPoint = PointF(left, top)
    private val northeastPoint = PointF(right, top)
    private val southeastPoint = PointF(right, bottom)
    private val southwestPoint = PointF(left, bottom)
    private val rotationPoint = PointF(centerX(), centerY())

    override fun offset(dx: Float, dy: Float) {
        val (acresX: Float, acresY: Float) = dx - offsetX to dy - offsetY
        offsetX += acresX
        offsetY += acresY
        super.offset(acresX, acresY)
        setPoints()
    }

    override fun set(left: Float, top: Float, right: Float, bottom: Float) { set(left, top, right, bottom, true) }
    fun set(left: Float, top: Float, right: Float, bottom: Float, notifyObservers: Boolean = true) {
        super.set(left, top, right, bottom)
        setPoints()
        if (notifyObservers) observers.forEach { it.onChange(this) }
    }

    override fun set(src: RectF) { set(src, true) }
    fun set(src: RectF, notifyObservers: Boolean = true) {
        set(src.left, src.top, src.right, src.bottom, notifyObservers)
    }

    override fun set(src: Rect) { set(src, true) }
    fun set(src: Rect, notifyObservers: Boolean = true) {
        val rectF = src.toRectF()
        set(rectF.left, rectF.top, rectF.right, rectF.bottom, notifyObservers)
    }

    fun addObserver(observer: OnBoundsChange) {
        observers.add(observer)
    }

    fun getCenter() = PointF(centerX(), centerY())

    fun removeObserver(observer: OnBoundsChange) {
        observers.remove(observer)
    }

    fun intersects(bounds: GameBounds) =
        super.intersects(bounds.left, bounds.top, bounds.right, bounds.bottom)

    override fun inset(dx: Float, dy: Float) {
        insetX = dx - insetX
        insetY = dy - insetY
        super.inset(insetX, insetY)
    }

    fun setCenter(point: PointF) = setCenter(point.x, point.y)
    fun setCenter(centerX: Float, centerY: Float) {
        val halfWidth = width() / 2
        val halfHeight = height() / 2
        left = centerX - halfWidth
        top = centerY - halfHeight
        right = centerX + halfWidth
        bottom = centerY + halfHeight
    }

    var scale = 1f
        set(value) {
            val originalWidth = width() / scale
            val originalHeight = height() / scale

            val scaleWidthDiff = (originalWidth * value) - width()
            val scaleHeightDiff = (originalHeight * value) - height()

            set(
                left = left - (scaleWidthDiff / 2),
                top = top - (scaleHeightDiff / 2),
                right = right + (scaleWidthDiff / 2),
                bottom = bottom + (scaleHeightDiff / 2)
            )
            field = value
        }

    fun rotate(degrees: Float, cx: Float = 0f, cy: Float = 0f) {
        rotationDegrees = degrees
        rotationPoint.set(cx, cy)
        val pts = floatArrayOf(
            northwestPoint.x, northwestPoint.y,
            northeastPoint.x, northeastPoint.y,
            southeastPoint.x, southeastPoint.y,
            southwestPoint.x, southwestPoint.y
        )
        rotationMatrix.setRotate(degrees, left + cx, top + cy)
        rotationMatrix.mapPoints(pts)
        northwestPoint.set(pts[0], pts[1])
        northeastPoint.set(pts[2], pts[3])
        southeastPoint.set(pts[4], pts[5])
        southwestPoint.set(pts[6], pts[7])
    }

    fun pathIntersect(src: Path) = path.op(src, Path.Op.INTERSECT)

    fun copy(): GameBounds = super.clone() as GameBounds

    private fun setPoints() {
        northwestPoint.set(left, top)
        northeastPoint.set(right, top)
        southeastPoint.set(right, bottom)
        southwestPoint.set(left, bottom)
        if (rotationDegrees != 0f) {
            rotate(rotationDegrees, rotationPoint.x, rotationPoint.y)
        }
    }
}

interface OnBoundsChange {
    fun onChange(rect: RectF)
}