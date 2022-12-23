package comx.y.z.collage.utils

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdSize
import com.ji.adshelper.ads.AdsHelper
import com.ji.adshelper.ads.AdsHelper.loadBanner
import com.ji.adshelper.ads.AdsHelper.loadInterstitialAd
import com.ji.adshelper.ads.AdsHelper.loadNative
import com.ji.adshelper.ads.AdsHelper.loadRewardAd
import com.ji.adshelper.ads.AdsHelper.showInterstitialAd
import com.ji.adshelper.ads.AdsHelper.showOneInterstitialAd
import com.ji.adshelper.ads.AdsHelper.showRewardAd
import com.ji.adshelper.view.TemplateView
import com.jibase.extensions.gone
import com.jibase.extensions.visible
import comx.y.z.collage.App
import ji.common.extensions.isOnline
import ji.purchase.BillingProcessor

object AdUtils {
    fun initNative(context: Context, group: ViewGroup, templateView: TemplateView) {
        if (BillingProcessor.isPurchased() || !context.isOnline()) {
            group.gone()
        } else if (!BillingProcessor.isPurchased()) {
            group.visible()
            loadNative(group, templateView, null)
        }
    }

    fun initBanner(context: Context, container: View, group: ViewGroup) {
        if (BillingProcessor.isPurchased() || !context.isOnline()) {
            container.gone()
        } else if (!BillingProcessor.isPurchased()) {
            container.visible()
            loadBanner(group, getAdSize(context), null)
        }
    }

    fun initBanner(
        context: Context,
        container: View,
        group: ViewGroup,
        hideRunnable: Runnable? = null
    ) {
        if (BillingProcessor.isPurchased() || !context.isOnline()) {
            container.gone()
            hideRunnable?.run()
        } else if (!BillingProcessor.isPurchased()) {
            container.visible()
            loadBanner(group, getAdSize(context), null)
        }
    }

    fun <T> initInterstitialAds(target: T, adLoadListener: AdsHelper.AdLoadListener? = null) {
        if (BillingProcessor.isPurchased()) return
        loadInterstitialAd(target, adLoadListener)
    }

    fun <T> showInterstitialAds(
        target: T,
        cacheNew: Boolean = true,
        listener: AdsHelper.AdListener? = null
    ) {
        if (BillingProcessor.isPurchased()) {
            listener?.onAdRewarded()
            return
        }
        App.context.isOpenFullAds = true
        showInterstitialAd(target, cacheNew, object : AdsHelper.AdListener() {
            override fun onAdLoadFailed() {
                App.context.isOpenFullAds = false
                listener?.onAdLoadFailed()
            }

            override fun onAdRewarded() {
                App.context.isOpenFullAds = false
                listener?.onAdRewarded()
            }
        })
    }

    private fun getAdSize(context: Context): AdSize {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        val outMetrics: DisplayMetrics = context.resources.displayMetrics
        val widthPixels: Float = outMetrics.widthPixels.toFloat()
        val density: Float = outMetrics.density
        val adWidth = (widthPixels / density).toInt()

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    fun initOneInterstitialAds(
        application: Application,
        adLoadListener: AdsHelper.AdLoadListener?
    ) {
        if (BillingProcessor.isPurchased()) return
        loadInterstitialAd<Context>(application.applicationContext, adLoadListener)
    }

    fun <T> showOneInterstitialAds(
        application: Application,
        listener: AdsHelper.AdListener? = null
    ) {
        if (BillingProcessor.isPurchased()) {
            listener?.onAdRewarded()
            return
        }
        showOneInterstitialAd(
            application.applicationContext,
            application.applicationContext,
            listener
        )
    }

    fun <T> initRewardAds(target: T, adLoadListener: AdsHelper.AdLoadListener?) {
        if (BillingProcessor.isPurchased()) return
        loadRewardAd(target, adLoadListener)
    }

    fun <T> showRewardAds(
        target: T,
        cacheNew: Boolean = false,
        listener: AdsHelper.AdListener? = null
    ) {
        if (BillingProcessor.isPurchased()) {
            listener?.onAdRewarded()
            return
        }
        App.context.isOpenFullAds = true
        showRewardAd(target, cacheNew, object : AdsHelper.AdListener() {
            override fun onAdLoadFailed() {
                App.context.isOpenFullAds = false
                listener?.onAdLoadFailed()
            }

            override fun onAdRewarded() {
                App.context.isOpenFullAds = false
                listener?.onAdRewarded()
            }
        })
    }
}