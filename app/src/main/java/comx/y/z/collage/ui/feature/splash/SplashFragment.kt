package comx.y.z.collage.ui.feature.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.ji.adshelper.ads.OnAdListener
import com.jibase.extensions.safeNavigate
import com.jibase.extensions.viewBinding
import comx.y.z.collage.App
import comx.y.z.collage.R
import comx.y.z.collage.constance.MAX_DELAY_START_MAIN
import comx.y.z.collage.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import ji.common.extensions.collectOne
import ji.common.ui.BaseFragment
import ji.purchase.BillingProcessor.isPurchased

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel by viewModels<SplashViewModel>()
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectOne(viewModel.eventGotoHome) {
            if (it) startMain()
        }

        viewModel.initFirst()

        FirebaseApp.initializeApp(requireContext())
        initAds()
        viewModel.putCountOpen()

        runAnimation()
    }

    override fun onDestroyView() {
        removeCallBack()
        super.onDestroyView()
    }

    private fun removeCallBack() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun startMain() {
        findNavController().safeNavigate(R.id.homeFragment)
    }

    private fun runAnimation() {
        val animation: Animation = TranslateAnimation(0f, 0f, 480f, 0f)
        animation.duration = 1000
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                viewModel.isEndedAnimation = true
                onEndAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding.afterLogoTv.startAnimation(animation)
    }


    private fun onEndAnimation() {
        if (!isPurchased() && App.context.openAdsHelper?.isAdAvailable == true) {
            removeCallBack()
            handler.postDelayed({ this.showAds() }, 1000)
        } else if (viewModel.hasAdsState) {
            viewModel.eventGotoHome.tryEmit(true)
        } else {
            // waiting load open ads
            handler.postDelayed({
                removeCallBack()
                viewModel.eventGotoHome.tryEmit(true)
            }, MAX_DELAY_START_MAIN)
        }
    }

    private fun initAds() {
        binding.tvMessageAds.visibility = if (!isPurchased()) View.VISIBLE else View.GONE
        if (!isPurchased()) {
            App.context.openAdsHelper?.fetchAd(object : OnAdListener() {
                override fun onAdLoaded() {
                    viewModel.hasAdsState = true
                    if (viewModel.isEndedAnimation) showAds()
                }

                override fun onAdLoadFailed(code: Int) {
                    viewModel.hasAdsState = true
                    if (viewModel.isEndedAnimation) viewModel.eventGotoHome.tryEmit(true)
                }
            })
        }
    }


    private fun showAds() {
        removeCallBack()
        App.context.openAdsHelper?.showAdIfAvailable(object : OnAdListener() {
            override fun onAdClosed() {
                viewModel.eventGotoHome.tryEmit(true)
            }

            override fun onAdLoadFailed(code: Int) {
                viewModel.eventGotoHome.tryEmit(true)
            }
        })
    }
}