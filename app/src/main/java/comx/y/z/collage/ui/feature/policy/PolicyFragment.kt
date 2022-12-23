package comx.y.z.collage.ui.feature.policy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jibase.extensions.popBack
import com.jibase.extensions.safeNavigate
import com.jibase.extensions.viewBinding
import com.jibase.extensions.visible
import comx.y.z.collage.R
import comx.y.z.collage.databinding.FragmentPrivacyPolicyBinding
import dagger.hilt.android.AndroidEntryPoint
import ji.common.ui.BaseFragment

@AndroidEntryPoint
class PolicyFragment : BaseFragment(R.layout.fragment_privacy_policy) {
    private val binding by viewBinding(FragmentPrivacyPolicyBinding::bind)
    private val viewModel by viewModels<PolicyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewModel.isFirstInstall() && arguments == null) {
            launchHomeScreen()
        } else {
            binding.rootView.visible()
        }
        binding.btnOpen.setOnClickListener {
            if (arguments?.getBoolean("data", false) == true) {
                popBack()
            } else {
                viewModel.setFirstInstalled()
                launchHomeScreen()
            }
        }

        binding.footerTv1.setOnClickListener {
            val uri =
                Uri.parse("https://docs.google.com/document/d/e/2PACX-1vScO2i5Fdcz9zfXP9IWoLJiWbv09SSziQQtxrSxsrg6GDYik8pxw3zW5XimfGEbaDFSvCsLTjUycuMx/pub")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun launchHomeScreen() {
        findNavController().safeNavigate(R.id.toSplash)
    }
}