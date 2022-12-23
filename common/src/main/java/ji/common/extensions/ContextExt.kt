package ji.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.TypedValue

@SuppressLint("MissingPermission")
fun Context.isOnline(): Boolean = try {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    activeNetworkInfo != null && activeNetworkInfo.isConnected
} catch (ex: Exception) {
    ex.printStackTrace()
    false
}

fun Context.checkAppInstalled(uri: String?): Boolean {
    val pm = packageManager
    try {
        pm.getPackageInfo(uri ?: return false, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}

fun Context.spToPx(sp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    ).toInt()
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}