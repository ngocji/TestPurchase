package comx.y.z.collage.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import com.jibase.utils.IntentUtils
import comx.y.z.collage.R
import comx.y.z.collage.data.model.User
import comx.y.z.collage.ui.dialog.ExitDialog
import ji.common.extensions.checkAppInstalled
import ji.common.extensions.isOnline
import ji.common.ui.BaseActivity
import ji.common.ui.ConfirmDialog
import ji.common.ui.confirmdialog.*
import ji.purchase.BillingProcessor

class MainActivity : BaseActivity(R.layout.activity_main) {
    private var rootRef: DatabaseReference? = null
    private var collionRef: DatabaseReference? = null
    private var headLine: String? = null
    private var description: String? = null
    private var appIcon: String? = null
    private var adImage: String? = null
    private var appLink: String? = null
    private var strpack: String? = null
    private var strnumber: Int? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRunning = true
        initTheme()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        rootRef = FirebaseDatabase.getInstance().reference
        collionRef = rootRef?.child("app58")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val collion = ds.getValue(User::class.java)
                    headLine = collion?.getName()
                    description = collion?.getDes()
                    appIcon = collion?.getImage()
                    adImage = collion?.getScreen()
                    appLink = collion?.getLink()
                    strpack = collion?.getPack()
                    strnumber = collion?.getNumber()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        collionRef?.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }

    private fun initTheme() {
        setTheme(viewModel.getTheme())
    }

//    val currentLanguage: Locale
//        get() {
//            val ordinate: Int = sharePref.getInt(PrefConst.PREF_LANG, Language.ENGLISH.ordinal())
//            return Language.safeValueOf(ordinate).getLocate()
//        }
//
//    fun setLanguage(language: Language) {
//        Lingver.getInstance().setLocale(this, language.getLocate())
//        sharePref.put(PrefConst.PREF_LANG, language.ordinal())
//    }
//
//    val currentLanguageAs: Language
//        get() {
//            val ordinate: Int = sharePref.getInt(PrefConst.PREF_LANG, Language.ENGLISH.ordinal())
//            return Language.safeValueOf(ordinate)
//        }

    override fun onBackPressed() {
        val nav = findNavController(R.id.nav_host_fragment)
        if (nav.previousBackStackEntry == null) {
            doFinishApp()
        } else {
            super.onBackPressed()
        }
    }

    fun getCurrentDestination() = findNavController(R.id.nav_host_fragment).currentDestination

    private fun doFinishApp() {
        when {
            isOnline() && strnumber != null && !checkAppInstalled(appLink) && !BillingProcessor.isPurchased() -> {
                ExitDialog(appIcon, adImage, headLine, description, appLink)
                    .show(supportFragmentManager)
            }
            isOnline() && viewModel.isShowRate() -> showRate()
            else -> finish()
        }
    }

    private fun showRate() {
        ConfirmDialog.newBuilder()
            .setTitle(ConfigText().setText(InfText.Resource(R.string.rate_us)))
            .setMessage(ConfigText().setText(InfText.Resource(R.string.rate_us_message)))
            .setButtonConfirm(
                ConfigButton()
                    .setText(InfText.Resource(R.string.yes))
            )
            .setButtonCancel(
                ConfigButton()
                    .setText(InfText.Resource(R.string.later))
            )

            .setConfig(
                ConfigDialog()
                    .setCallBack(object : DialogConfirmCallBack {
                        override fun onConfirmClicked(dialog: ConfirmDialog?) {
                            IntentUtils.goToStore(this@MainActivity, packageName)
                        }

                        override fun onCancelClicked(dialog: ConfirmDialog) {
                            finish()
                        }
                    })
            )
            .build()
            .show(supportFragmentManager)
    }


    companion object {
        var isRunning = false
        fun start(context: Context, clear: Boolean) {
            val intent = Intent(context, MainActivity::class.java)
            if (clear) {
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}