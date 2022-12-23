package comx.y.z.collage.ui.base

import android.os.Bundle
import android.view.View
import com.jibase.extensions.observe
import ji.common.ui.BaseFragment
import ji.purchase.BillingProcessor

abstract class BasePurchaseFragment(layout: Int) : BaseFragment(layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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