package ji.common.helper

import android.net.Uri
import androidx.core.net.toUri
import java.io.File

fun Uri.isValidScheme(): Boolean = scheme != null && scheme != "null"

fun getUriFromPath(path: String): Uri {
    val file = File(path)
    return when {
        file.exists() -> file.toUri()
        else -> Uri.parse(path)
    }
}