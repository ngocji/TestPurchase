package ji.common.helper

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import java.io.File

object DownloadImageHelper {
    suspend fun download(context: Context, url: String, cacheKey: String): File {
        return Glide.with(context)
            .download(object : GlideUrl(url) {
                override fun getCacheKey(): String = cacheKey
            })
            .submit()
            .get()
    }
}