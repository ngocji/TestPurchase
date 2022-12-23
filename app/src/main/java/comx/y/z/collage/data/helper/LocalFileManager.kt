package comx.y.z.collage.data.helper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

class LocalFileManager(@ApplicationContext private val context: Context) {
    companion object {
    }

    @Suppress("SameParameterValue")
    private fun getSuitableFile(folder: File, prefix: String): File {
        val timeStamp = System.currentTimeMillis()
        var file: File
        var expectedIndex = 0
        do {
            val name = "${prefix}_${timeStamp}" + if (expectedIndex == 0) "" else "_$expectedIndex"
            file = File(folder, name)
            expectedIndex++
        } while (file.exists() && file.length() > 0)
        return file
    }
}