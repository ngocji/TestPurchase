package ji.common.retriever

import com.jibase.helper.GsonManager
import com.jibase.pref.SharePref
import ji.common.helper.FileBackupHelper
import ji.common.helper.readTextFromInputStream
import ji.common.helper.writeToFile
import java.io.File
import java.lang.reflect.Type

abstract class BaseFileDataRetriever<T>(
    prefLastRefreshTime: String,
    refreshInterval: Long,
    sharePref: SharePref,
    private val file: File,
    private val type: Type
) : BaseDataRetriever<T>(prefLastRefreshTime, refreshInterval, sharePref) {

    private val fileBackupHelper = FileBackupHelper(file)

    override suspend fun getLocal(): T {
        val data = fileBackupHelper.read {
            readTextFromInputStream(file.inputStream())
        }
        return GsonManager.fromJson<T>(data, type)
            ?: throw IllegalStateException("No data in local")
    }

    override suspend fun saveToLocal(data: T) {
        fileBackupHelper.write {
            writeToFile(GsonManager.toJson(data), file)
        }
    }
}