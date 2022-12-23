package comx.y.z.collage.ui

import androidx.lifecycle.ViewModel
import com.jibase.pref.SharePref
import comx.y.z.collage.R
import comx.y.z.collage.constance.PREF_SHOW_RATE
import comx.y.z.collage.constance.PREF_THEME_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharePref: SharePref
) : ViewModel() {

    fun getTheme(): Int = sharePref.getInt(PREF_THEME_ID, R.style.ThemeApp)

    fun isShowRate(): Boolean = sharePref.getBoolean(PREF_SHOW_RATE, true)

    fun setShowRate(b: Boolean) {
        sharePref.putBoolean(PREF_SHOW_RATE, b)
    }
}