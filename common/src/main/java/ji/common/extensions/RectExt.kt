package ji.common.extensions

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF

fun makeRectF(left: Int, top: Int, right: Int, bottom: Int): RectF =
    RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

fun makeRect(left: Float, top: Float, right: Float, bottom: Float): Rect =
    Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

fun RectF.toRect() = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

fun List<PointF>.getBoundRect(): RectF {
    val left = (minByOrNull { it.x }?.x ?: 0f)
    val top = (minByOrNull { it.y }?.y ?: 0f)
    val right = (maxByOrNull { it.x }?.x ?: 0f)
    val bottom = (maxByOrNull { it.y }?.y ?: 0f)
    return RectF(left, top, right, bottom)
}