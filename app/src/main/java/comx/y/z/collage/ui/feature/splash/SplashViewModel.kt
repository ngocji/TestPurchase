package comx.y.z.collage.ui.feature.splash

import androidx.lifecycle.ViewModel
import com.jibase.pref.SharePref
import comx.y.z.collage.BuildConfig
import comx.y.z.collage.constance.PREF_APP_VERSION
import comx.y.z.collage.constance.PREF_COUNT_OPEN_APP
import comx.y.z.collage.constance.PREF_FIRST_TIME_INSTALL_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharePref: SharePref
) : ViewModel() {
    var isEndedAnimation = false
    var hasAdsState = false

    var eventGotoHome = MutableStateFlow<Boolean?>(null)

    fun initFirst() {
        sharePref.put(PREF_APP_VERSION, BuildConfig.VERSION_CODE) // put sharePref app version
        sharePref.put(PREF_FIRST_TIME_INSTALL_APP, false)
    }

    fun putCountOpen() {
        sharePref.put(PREF_COUNT_OPEN_APP, sharePref.getInt(PREF_COUNT_OPEN_APP, 0) + 1)
    }
}