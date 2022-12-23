package ji.common.ui.confirmdialog

import android.view.Gravity
import androidx.annotation.GravityInt
import androidx.annotation.StyleRes

class ConfigButton {
    var text: InfText? = null
    var textColor: InfTextColor? = null
    var background: InfBackground? = null
    var icon: InfIcon? = null

    @StyleRes
    var textStyle = 0

    @GravityInt
    var textGravity: Int = Gravity.CENTER


    fun setText(text: InfText): ConfigButton {
        this.text = text
        return this
    }


    fun setTextColor(color: InfTextColor): ConfigButton {
        this.textColor = color
        return this
    }

    fun setTextStyle(@StyleRes style: Int): ConfigButton {
        this.textStyle = style
        return this
    }

    fun setGravity(@GravityInt gravity: Int): ConfigButton {
        this.textGravity = gravity
        return this
    }

    fun setIcon(icon: InfIcon): ConfigButton {
        this.icon = icon
        return this
    }

    fun setBackground(background: InfBackground): ConfigButton {
        this.background = background
        return this
    }
}