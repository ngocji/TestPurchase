package comx.y.z.collage.ui.feature.home

import androidx.lifecycle.ViewModel
import com.jibase.pref.SharePref
import comx.y.z.collage.constance.PREF_FIRST_TIME_INSTALL_APP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharePref: SharePref
) : ViewModel() {

}