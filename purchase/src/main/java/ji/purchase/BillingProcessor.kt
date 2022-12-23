package ji.purchase

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.android.billingclient.api.ProductDetails
import com.ji.adshelper.biling.BillingService
import com.ji.adshelper.biling.IapHelper
import com.ji.adshelper.biling.IapHelperImpl
import com.ji.adshelper.biling.entities.DataWrappers
import com.ji.adshelper.biling.extension.toMapSUBS
import com.ji.adshelper.biling.listener.BillingServiceListener

object BillingProcessor {

    lateinit var iapHelper: IapHelper

    @JvmField
    val eventPurchaseSuccess = IPurchaseLiveEvent<DataWrappers.PurchaseInfo>()
    private val onPurchaseListener = OnPurchaseListener()

    lateinit var configPurchase: ConfigPurchase

    fun initBillingService(app: Application, config: ConfigPurchase) {
        iapHelper = IapHelperImpl(app.applicationContext)
        configPurchase = config

        BillingService.init(
            app,
            nonConsumableSkus = config.nonConsumeProducts,
            consumableSkus = config.consumeProducts,
            subscriptionSkus = config.subscriptionProducts,
            decodedKey = config.licenseKey
        )

        BillingService.setListener(onPurchaseListener)
    }

    @JvmStatic
    fun purchase(activity: Activity, productId: String) {
        if (!iapHelper.isItemPurchased(productId)) {
            val launchSuccessfully =
                BillingService.launchBillingFlow(activity, productId)
            if (!launchSuccessfully) {
                iapHelper.showPurchaseErrorMessage()
            }
        }
    }

    @JvmStatic
    fun getPurchased(): ProductDetails? {
        val id =
            configPurchase.nonConsumeProducts.find { iapHelper.isItemPurchased(it) }.let { sku ->
                sku ?: configPurchase.subscriptionProducts.find { iapHelper.isItemPurchased(it) }
            } ?: return null

        return BillingService.getProductDetail(id)
    }

    @JvmStatic
    fun queryPurchasedInfo(
        type: String,
        productId: String,
        action: (DataWrappers.PurchaseInfo) -> Unit
    ) {
        BillingService.queryPurchasedInfo(type, productId, action)
    }

    @JvmStatic
    fun restorePurchase() {
        BillingService.restorePurchase()
    }

    @JvmStatic
    fun isPurchased(): Boolean {
        return configPurchase.nonConsumeProducts.any { iapHelper.isItemPurchased(it) } || configPurchase.subscriptionProducts.any {
            iapHelper.isItemPurchased(
                it
            )
        }
    }

    @JvmStatic
    fun openManager(context: Context) {
        val sku: String = getPurchasedProductId()
        if (TextUtils.isEmpty(sku)) return
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions?sku=" + sku + "&package=" + context.getPackageName())
                )
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Error open manager purchased!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun getProductDetails() = BillingService.getProductDetails().map { entry ->
        entry.key to entry.value?.toMapSUBS()
    }.toMap()

    @JvmStatic
    fun getPurchasedProductId(): String {
        val subId = configPurchase.subscriptionProducts.find { iapHelper.isItemPurchased(it) }
        if (subId != null) return subId

        return configPurchase.nonConsumeProducts.find { iapHelper.isItemPurchased(it) } ?: ""
    }

    fun unSubscription(activity: Activity) {
        BillingService.unsubscribe(activity, "");
    }

    class OnPurchaseListener : BillingServiceListener {

        override fun onPurchaseError(isUserCancelled: Boolean) {
            if (!isUserCancelled) {
                iapHelper.showPurchaseErrorMessage()
            }
        }

        override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
            savePrefIfNeed(purchaseInfo)
            eventPurchaseSuccess.postValue(purchaseInfo)
            iapHelper.showPurchaseSuccessMessage()
        }

        override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
            val revokedNonConsumePrdIds =
                configPurchase.nonConsumeProducts.filter { purchaseInfo.sku != it }
            val revokedSubPrdIds =
                configPurchase.subscriptionProducts.filter { purchaseInfo.sku != it }

            revokedNonConsumePrdIds.forEach { iapHelper.onProductRevoked(it) }
            revokedSubPrdIds.forEach { iapHelper.onProductRevoked(it) }

            savePrefIfNeed(purchaseInfo)
            eventPurchaseSuccess.postValue(purchaseInfo)
        }

        private fun savePrefIfNeed(purchaseInfo: DataWrappers.PurchaseInfo) {
            // save pref for non consume and subs
            if (configPurchase.nonConsumeProducts.any { it == purchaseInfo.sku } || configPurchase.subscriptionProducts.any { it == purchaseInfo.sku }) {
                iapHelper.onProductPurchased(purchaseInfo.sku)
            }
        }
    }
}