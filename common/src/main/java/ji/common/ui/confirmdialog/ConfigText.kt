package ji.common.ui.confirmdialog

import android.view.Gravity
import androidx.annotation.GravityInt
import androidx.annotation.StyleRes

class ConfigText {
    var text: InfText? = null
    var textColor: InfTextColor? = null

    @GravityInt
    var textGravity: Int = Gravity.CENTER


    fun setText(text: InfText): ConfigText {
        this.text = text
        return this
    }


    fun setTextColor(color: InfTextColor): ConfigText {
        this.textColor = color
        return this
    }

    fun setGravity(@GravityInt gravity: Int): ConfigText {
        this.textGravity = gravity
        return this
    }
}