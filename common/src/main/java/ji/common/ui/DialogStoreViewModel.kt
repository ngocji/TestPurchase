package ji.common.ui

import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

class DialogStoreViewModel : ViewModel() {
    private val data = mutableSetOf<Any?>()

    fun <T : Any> put(vararg data: T?) {
        this.data.addAll(data)
    }

    fun <T : Any> get(clazz: KClass<T>) = data.find { it != null && it::class == clazz } as? T

    override fun onCleared() {
        super.onCleared()
        data.clear()
    }
}