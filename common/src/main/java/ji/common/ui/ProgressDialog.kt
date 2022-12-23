package ji.common.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jibase.ui.dialog.BaseDialog
import ji.common.R

class ProgressDialog : BaseDialog(R.layout.dialog_progress) {
    private var count: Int = 0
    override fun onViewReady(savedInstanceState: Bundle?) {
        isCancelable = false
    }

    fun safeDismiss() {
        count--

        if (dialog != null && dialog?.isShowing == true && count <= 0) {
            dismissAllowingStateLoss()
        }
    }

    fun forceDismiss() {
        count = 0
        if (dialog != null && dialog?.isShowing == true && count <= 0) {
            dismissAllowingStateLoss()
        }
    }

    fun show(fragment: Fragment) {
        count++
        if (fragment.childFragmentManager.findFragmentByTag(javaClass.name) != null) return
        showNow(fragment.childFragmentManager, javaClass.name)
    }

    fun show(activity: AppCompatActivity) {
        count++
        if (activity.supportFragmentManager.findFragmentByTag(javaClass.name) != null) return
        showNow(activity.supportFragmentManager, javaClass.name)
    }
}