package ji.common.helper

import android.content.Context
import android.graphics.Typeface
import java.io.File

object TypefaceHelper {
    private val hasTypeface = hashMapOf<String, Typeface>()

    /**
     * Create font and put to map
     * @param path: name of font in asset, ex: italic.ttf
     */
    fun getOrInitTypeFace(context: Context, path: String): Typeface? {
        return try {
            hasTypeface.getOrPut(path) {
                val file = File(path)
                if (file.exists()) {
                    Typeface.createFromFile(file)
                } else {
                    Typeface.createFromAsset(
                        context.assets,
                        path.replace("file:///android_asset/", "")
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}