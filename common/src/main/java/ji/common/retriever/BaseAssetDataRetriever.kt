package ji.common.retriever

abstract class BaseAssetDataRetriever<T> {
    private var data: T? = null

    abstract suspend fun getLocal(): T

    suspend fun getData(): T {
        return when (data) {
            null -> getLocal().apply {
                data = this
            }
            else -> data ?: throw NullPointerException("Null data")
        }
    }
}