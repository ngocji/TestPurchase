package ji.common.retriever

import com.jibase.pref.SharePref

abstract class BaseDataRetriever<T>(
    private val prefLastRefreshTime: String,
    private val refreshInterval: Long,
    private val sharePref: SharePref
) {
    private var data: T? = null

    abstract suspend fun getRemote(): T?
    abstract suspend fun getLocal(): T?
    abstract suspend fun saveToLocal(data: T)

    suspend fun getData(): T? {
        return when (data) {
            null -> {
                when {
                    needRefresh() -> {
                        getRemote()?.apply { saveToLocal(this) } ?: getLocal()
                    }

                    else -> getLocal()
                }.apply {
                    data = this
                }
            }
            else -> data ?: throw NullPointerException("Empty data")
        }
    }

    fun isAvailableData() = data != null

    private fun needRefresh(): Boolean {
        val lastTimeLoaded = sharePref.getLong(prefLastRefreshTime, 0L)
        val currentTime = System.currentTimeMillis()
        return currentTime - lastTimeLoaded > refreshInterval
    }

    private fun setRefreshTime() {
        sharePref.put(prefLastRefreshTime, System.currentTimeMillis())
    }
}