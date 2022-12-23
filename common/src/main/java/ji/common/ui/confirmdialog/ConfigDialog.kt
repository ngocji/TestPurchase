package ji.common.ui.confirmdialog

class ConfigDialog {
    var icon: InfIcon? = null
    var isCancelable = true
    var isShowCancelButton = true
    var isShowConfirmButton = true
    var closeIcon: InfIcon? = null
    var dismissWhenClick: Boolean = true
    var callBack: DialogConfirmCallBack? = null
    var background: InfBackground? = null

    fun setIcon(icon: InfIcon): ConfigDialog {
        this.icon = icon
        return this
    }

    fun setCancelable(isCancelable: Boolean): ConfigDialog {
        this.isCancelable = isCancelable
        return this
    }

    fun setShowCancelButton(show: Boolean): ConfigDialog {
        this.isShowCancelButton = show
        return this
    }

    fun setShowConfirmButton(show: Boolean): ConfigDialog {
        this.isShowConfirmButton = show
        return this
    }

    fun setCallBack(callBack: DialogConfirmCallBack): ConfigDialog {
        this.callBack = callBack
        return this
    }

    fun setCloseIcon(icon: InfIcon): ConfigDialog {
        this.closeIcon = icon
        return this
    }

    fun setDismissWhenClick(dismiss: Boolean): ConfigDialog {
        this.dismissWhenClick = dismiss
        return this
    }

    fun setBackground(background: InfBackground): ConfigDialog {
        this.background = background
        return this
    }
}