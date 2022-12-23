package ji.common.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun Uri.fullFileName(context: Context): String? {
    val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
    val cr: ContentResolver = context.contentResolver
    return cr.query(this, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            return@use cursor.getString(0)
        }
        return@use null
    }
}
