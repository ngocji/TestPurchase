package ji.common.ui.confirmdialog

import ji.common.ui.ConfirmDialog

interface DialogConfirmCallBack {
    fun onConfirmClicked(dialog: ConfirmDialog?) {}
    fun onCancelClicked(dialog: ConfirmDialog) {}
    fun onDismiss() {}
}