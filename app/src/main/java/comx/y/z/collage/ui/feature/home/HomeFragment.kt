package comx.y.z.collage.ui.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jibase.extensions.viewBinding
import comx.y.z.collage.R
import comx.y.z.collage.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import ji.common.ui.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}