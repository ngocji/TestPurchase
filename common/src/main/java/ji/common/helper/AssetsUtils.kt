package ji.common.helper

import android.content.Context
import java.io.File

object AssetsUtils {
    fun getListItemFromAsset(context: Context, assetPath: String): List<String> {
        return context.assets.list(assetPath)?.toList() ?: emptyList()
    }

    fun formatAssetsName(assetFilePath: String): String {
        val assetName = File(assetFilePath).name
        val nameResult = assetName.replace("_", " ")
        return nameResult[0].uppercaseChar() + nameResult.substring(1, nameResult.length)
    }
}