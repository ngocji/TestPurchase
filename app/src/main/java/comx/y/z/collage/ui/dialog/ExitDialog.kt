package comx.y.z.collage.ui.dialog

import android.os.Bundle
import com.jibase.extensions.load
import com.jibase.extensions.viewBinding
import com.jibase.ui.dialog.BaseDialog
import com.jibase.utils.IntentUtils
import comx.y.z.collage.R
import comx.y.z.collage.databinding.DialogExitBinding

class ExitDialog(
    private val appIcon: String?,
    private val adImage: String?,
    private val headLine: String?,
    private val description: String?,
    private val appLink: String?
): BaseDialog(R.layout.dialog_exit) {
    private val binding by viewBinding(DialogExitBinding::bind)

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.adAppIcon.load(appIcon)
        binding.adImage.load(adImage)
        binding.adHeadline.text = headLine
        binding.adBody.text = description


        binding.btndown.setOnClickListener {
            dismissAllowingStateLoss()
            IntentUtils.goToStore(requireContext(), appLink ?: return@setOnClickListener)
        }

        binding.btnexit.setOnClickListener {
            dismissAllowingStateLoss()
            activity?.finish()
        }
    }
}