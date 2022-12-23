package ji.common.ui.confirmdialog

import android.graphics.Bitmap
import android.view.Gravity
import androidx.annotation.*

sealed class InfText {
    data class Text(val text: String) : InfText()
    data class Resource(@StringRes val textRes: Int) : InfText()
}


sealed class InfTextColor {
    data class Color(@ColorInt val color: Int) : InfTextColor()
    data class Resource(@ColorRes val color: Int) : InfTextColor()
    data class Style(@StyleRes val style: Int) : InfTextColor()
}

sealed class InfBackground {
    data class Color(@ColorInt val color: Int) : InfBackground()
    data class ColorResource(@ColorRes val color: Int) : InfBackground()
    data class DrawableResource(@DrawableRes val drawable: Int) : InfBackground()
}


sealed class InfIcon(val gravity: Int = Gravity.START) {
    class Icon(val bitmap: Bitmap, gravity: Int = Gravity.START) : InfIcon(gravity)
    class IconResource(@DrawableRes val res: Int, gravity: Int = Gravity.START) : InfIcon(gravity)
}
