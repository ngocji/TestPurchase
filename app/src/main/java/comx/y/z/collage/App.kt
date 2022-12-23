package comx.y.z.collage

import android.app.Activity
import android.app.Application
import com.ji.adshelper.ads.AdsSDK
import com.ji.adshelper.ads.OpenAdsHelper
import com.yariksoffice.lingver.Lingver
import comx.y.z.collage.ui.MainActivity
import dagger.hilt.android.HiltAndroidApp
import ji.purchase.BillingProcessor
import ji.purchase.BillingProcessor.isPurchased
import ji.purchase.ConfigPurchase
import java.util.*

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var context: App
    }

    var openAdsHelper: OpenAdsHelper? = null
    var isOpenFullAds = false

    override fun onCreate() {
        super.onCreate()
        context = this
        Lingver.init(this, Locale.ENGLISH)
        BillingProcessor.initBillingService(
            this, ConfigPurchase(
                nonConsumeProducts = listOf("com.fivestars.womenworkout_monthly"),
                subscriptionProducts = listOf(
                    "com.fivestars.womenworkout_monthly",
                    "com.fivestars.womenworkout_yearly30"
                ),
                licenseKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo6S6O6s2z9agPiCen8ebsfGKw8TqRy912EAzFJKXqSJ8SQc8TIQ1j/bJNj7m75c8yHaytQQFDCUfL3JV/+wyJl7XW/5LNtDQ1SG8L0VTqhPaFxwVtNMZU2Gb+jqBAOdKjOUFOW7tGtELojl799Ucf2S3b0E7Gx3DX1hBAMJOB2uqOEHaOljimIe8UNsdyTcYiYyWnuBtqYUeATuMbQOxHjJxsolvoBlol5EPtFxqFv0n9VFFRrfuucyX8TQ69J+tvFUR9SGonZHAX/3A0EGUwLjHFSjHWkXZDhKxSGgkokH+Mcg4PXDBe8MHhyidEx5DKFbNPp8O+lI1yqpPfTjgaQIDAQAB"
            )
        )

        AdsSDK.init(
            this,
            getString(R.string.banner_ad_unit_id),
            getString(R.string.goc_ad_unit_id),
            getString(R.string.ad_unit_id),
            getString(R.string.reward_ad_unit_id),
            getString(R.string.open_ad_unit_id),
            BuildConfig.DEBUG
        )

        openAdsHelper = OpenAdsHelper.init(this)
        openAdsHelper?.setValidShowAds { activity: Activity? ->
            var isPending = false
            if (activity is MainActivity) {
                val destination = (activity as? MainActivity)?.getCurrentDestination()
                if (destination != null && (destination.id == R.id.splashFragment || destination.id == R.id.policyFragment)) {
                    isPending = true
                }
            }
            !isPending && !isOpenFullAds && !isPurchased()
        }
    }

    fun pendingOpenAds(pending: Boolean) {
        openAdsHelper?.pendingShowAds = pending
    }
}