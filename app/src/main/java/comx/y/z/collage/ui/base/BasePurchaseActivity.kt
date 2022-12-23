package comx.y.z.collage.ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.jibase.extensions.observe
import ji.common.ui.BaseBindingActivity
import ji.purchase.BillingProcessor
import kotlin.reflect.KClass

abstract class BasePurchaseActivity<BINDING : ViewBinding>(bindingClass: KClass<BINDING>) :
    BaseBindingActivity<BINDING>(bindingClass) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe(BillingProcessor.eventPurchaseSuccess) {
            if (BillingProcessor.isPurchased()) {
                onPurchased()
            }
        }
    }

    open fun onPurchased() {

    }

    fun isCanShowAds() = !BillingProcessor.isPurchased()
}