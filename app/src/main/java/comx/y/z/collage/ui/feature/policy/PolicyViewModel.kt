package comx.y.z.collage.ui.feature.policy

import androidx.lifecycle.ViewModel
import com.jibase.pref.SharePref
import comx.y.z.collage.constance.PREF_FIRST_TIME_INSTALL_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    private val sharePref: SharePref
) : ViewModel() {
    fun isFirstInstall() = sharePref.getBoolean(PREF_FIRST_TIME_INSTALL_APP, defaultValue = true)

    fun setFirstInstalled() {
        sharePref.putBoolean(PREF_FIRST_TIME_INSTALL_APP, false)
    }
}