package ji.common.extensions

import kotlinx.coroutines.flow.MutableStateFlow


fun <T> MutableStateFlow<T?>.clear() {
    tryEmit(null)
}