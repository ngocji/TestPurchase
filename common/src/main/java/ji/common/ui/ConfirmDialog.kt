package ji.common.ui

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.TextViewCompat
import com.google.android.material.button.MaterialButton
import com.jibase.extensions.gone
import com.jibase.extensions.viewBinding
import com.jibase.extensions.visible
import com.jibase.ui.dialog.BaseDialog
import ji.common.R
import ji.common.databinding.DialogConfirmBinding
import ji.common.ui.confirmdialog.*

class ConfirmDialog : BaseDialog(R.layout.dialog_confirm) {

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    private val binding by viewBinding(DialogConfirmBinding::bind)
    private var isNotifyConfirm = false

    private var builder: Builder? = null


    override fun initStyle(): Int {
        return R.style.style_dialog_70
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        builder?.also {
            applyConfigDialog(it.config)
            applyTextConfig(binding.tvTitle, it.title)
            applyTextConfig(binding.tvSubTitle, it.subTitle)
            applyTextConfig(binding.tvMessage, it.message)
            applyButtonConfig(binding.buttonConfirm, it.buttonConfirm)
            applyButtonConfig(binding.buttonCancel, it.buttonCancel)
        }

        binding.buttonConfirm.setOnClickListener { onConfirmClicked() }
        binding.buttonCancel.setOnClickListener { onCancelClicked() }
//        binding.buttonClose.setOnClickListener { onCancelClicked() }
    }

    private fun onCancelClicked() {
        if (isDismissWhenClick()) {
            dismissAllowingStateLoss()
        }
        getCallBack()?.onCancelClicked(this)
    }

    private fun onConfirmClicked() {
        isNotifyConfirm = true
        if (isDismissWhenClick()) {
            dismissAllowingStateLoss()
        }
        getCallBack()?.onConfirmClicked(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!isNotifyConfirm) getCallBack()?.onDismiss()
    }

    private fun applyConfigDialog(config: ConfigDialog?) {
        config ?: return
        config.icon?.also {
            with(binding.imageIcon) {
                visible()
                when (it) {
                    is InfIcon.Icon -> setImageBitmap(it.bitmap)
                    is InfIcon.IconResource -> setImageResource(it.res)
                }

                updateLayoutParams<LinearLayout.LayoutParams> {
                    gravity = it.gravity
                }
            }
        }

        isCancelable = config.isCancelable

        if (!config.isShowConfirmButton) {
            binding.buttonConfirm.gone()
        }

        if (!config.isShowCancelButton) {
            binding.buttonCancel.gone()
        }


        binding.llBackground.applyBackgroundInternal(config.background)
    }

    private fun applyTextConfig(textView: TextView, configText: ConfigText?) {
        configText ?: return

        with(textView) {
            if (applyTextInternal(configText.text)) return

            applyTextColorInternal(configText.textColor)

            applyGravity(configText.textGravity)
        }
    }

    private fun applyButtonConfig(button: MaterialButton, configButton: ConfigButton?) {
        configButton ?: return
        with(button) {
            applyTextInternal(configButton.text)
            applyTextColorInternal(configButton.textColor)
            applyBackgroundInternal(configButton.background)
        }
    }

    private fun TextView.applyGravity(textGravity: Int) {
        if (textGravity != Gravity.CENTER) {
            updateLayoutParams<LinearLayout.LayoutParams> {
                gravity = textGravity
            }
        }
    }

    private fun TextView.applyTextColorInternal(color: InfTextColor?) {
        when (color) {
            is InfTextColor.Color -> setTextColor(color.color)
            is InfTextColor.Resource -> setTextColor(
                ContextCompat.getColor(
                    context,
                    color.color
                )
            )
            is InfTextColor.Style -> TextViewCompat.setTextAppearance(this, color.style)
            else -> {}
        }
    }

    private fun TextView.applyTextInternal(text: InfText?): Boolean {
        when (text) {
            is InfText.Text -> setText(text.text)
            is InfText.Resource -> setText(text.textRes)
            else -> {
                gone()
                return true
            }
        }
        visible()
        return false
    }

    private fun View.applyBackgroundInternal(background: InfBackground?) {
        background ?: return
        when (background) {
            is InfBackground.Color -> {
                when (this) {
                    is MaterialButton -> backgroundTintList =
                        ColorStateList.valueOf(background.color)
                    else -> setBackgroundColor(background.color)
                }
            }

            is InfBackground.ColorResource -> {
                val color = ContextCompat.getColor(context, background.color)

                when (this) {
                    is MaterialButton -> backgroundTintList = ColorStateList.valueOf(color)
                    else -> setBackgroundColor(color)
                }
            }

            is InfBackground.DrawableResource -> {
                when (this) {
                    is MaterialButton -> {
                        backgroundTintList = ColorStateList.valueOf(0)
                    }
                }

                setBackgroundResource(background.drawable)
            }

            else -> {}
        }
    }

    private fun getCallBack(): DialogConfirmCallBack? {
        return builder?.config?.callBack
    }

    private fun isDismissWhenClick() =
        builder?.config?.dismissWhenClick ?: true

    class Builder {
        var config: ConfigDialog? = null
        var title: ConfigText? = null
        var subTitle: ConfigText? = null
        var message: ConfigText? = null
        var buttonConfirm: ConfigButton? = null
        var buttonCancel: ConfigButton? = null

        fun setConfig(config: ConfigDialog): Builder {
            this.config = config
            return this
        }

        fun setTitle(title: ConfigText): Builder {
            this.title = title
            return this
        }

        fun setSubTitle(subTitle: ConfigText): Builder {
            this.subTitle = subTitle
            return this
        }

        fun setMessage(message: ConfigText): Builder {
            this.message = message
            return this
        }

        fun setButtonConfirm(buttonConfirm: ConfigButton): Builder {
            this.buttonConfirm = buttonConfirm
            return this
        }

        fun setButtonCancel(buttonCancel: ConfigButton): Builder {
            this.buttonCancel = buttonCancel
            return this
        }


        fun build(): ConfirmDialog {
            val dialog = ConfirmDialog().apply {
                builder = this@Builder
            }

            return dialog
        }
    }
}
