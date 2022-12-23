package ji.common.flow

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

typealias SuccessHandler<T> = (value: T?) -> Unit
typealias LoadingHandler = (Boolean) -> Boolean
typealias InitHandler = () -> Unit
typealias ErrorHandler = (Throwable?) -> Unit

fun <T> LifecycleCoroutineScope.collect(
    flow: StateFlow<ResultWrapper<T>>,
    successHandler: SuccessHandler<T>? = null,
    errorHandler: ErrorHandler? = null,
    initHandler: InitHandler? = null,
    loadingHandler: LoadingHandler? = null,
) {
    launch {
        flow.collect {
            when (it) {
                is ResultWrapper.Success<T> -> {
                    loadingHandler?.invoke(false)
                    successHandler?.invoke(it.value)
                }
                is ResultWrapper.Error -> {
                    loadingHandler?.invoke(false)
                    errorHandler?.invoke(it.throwable)
                }

                is ResultWrapper.Loading -> {
                    loadingHandler?.invoke(true)
                }

                is ResultWrapper.None -> {
                    initHandler?.invoke()
                }
            }

            // clear
            if (flow is MutableStateFlow) {
                flow.tryEmit(ResultWrapper.None)
            }
        }
    }
}

fun <T> LifecycleCoroutineScope.pagingCollect(
    flow: StateFlow<ResultWrapper<T>>,
    successHandler: SuccessHandler<T>?,
    errorHandler: ErrorHandler? = null,
    loadingHandler: LoadingHandler? = null,
    initHandler: InitHandler? = null,
) {
    launch {
        flow.collect {
            when (it) {
                is ResultWrapper.Success<T> -> {
                    loadingHandler?.invoke(false)
                    successHandler?.invoke(it.value)
                }
                is ResultWrapper.Error -> {
                    loadingHandler?.invoke(false)
                    errorHandler?.invoke(it.throwable)
                }

                is ResultWrapper.Loading -> {
                    loadingHandler?.invoke(true)
                }

                ResultWrapper.None -> {
                    initHandler?.invoke()
                }
            }

            // clear
            if (flow is MutableStateFlow) {
                flow.tryEmit(ResultWrapper.None)
            }
        }
    }
}

fun <T> ViewModel.resultFlow(
    stateFlow: MutableStateFlow<ResultWrapper<T>> = MutableStateFlow(ResultWrapper.None),
    context: CoroutineContext = Dispatchers.IO,
    callback: suspend () -> ResultWrapper<T>
): MutableStateFlow<ResultWrapper<T>> = stateFlow.apply {
    viewModelScope.launch {
        with(this@resultFlow) {
            tryEmit(ResultWrapper.Loading)
            val resultData = withContext(context) {
                try {
                    callback.invoke()
                } catch (e: Exception) {
                    e.printStackTrace()
                    ResultWrapper.Error(e)
                }
            }
            try {
                value = resultData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun <T> ViewModel.run(
    context: CoroutineContext = Dispatchers.IO,
    callback: suspend () -> ResultWrapper<T>
) {
    viewModelScope.launch {
        with(this@run) {
            withContext(context) {
                try {
                    callback.invoke()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun <T> ViewModel.flow(
    flow: MutableStateFlow<T?>,
    context: CoroutineContext = Dispatchers.IO,
    callback: suspend () -> T
): MutableStateFlow<T?> = flow.apply {
    viewModelScope.launch {
        with(this) {
            val resultData = withContext(context) {
                try {
                    callback.invoke()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
            tryEmit(resultData)
        }
    }
}

fun <T> ViewModel.pagingFlow(
    flow: MutableStateFlow<ResultWrapper<List<T>>>,
    context: CoroutineContext = Dispatchers.IO,
    isFistPage: () -> Boolean,
    nextPage: (List<T>) -> Unit,
    callback: suspend () -> ResultWrapper<List<T>>
): MutableStateFlow<ResultWrapper<List<T>>> = flow.apply {
    viewModelScope.launch {
        with(this@pagingFlow) {
            if (isFistPage.invoke()) {
                tryEmit(ResultWrapper.Loading)
            }

            val result = withContext(context) {
                try {
                    callback.invoke()
                } catch (e: Exception) {
                    e.printStackTrace()
                    ResultWrapper.Error(e)
                }
            }

            when (result) {
                is ResultWrapper.Success -> {
                    if (isFistPage.invoke() && result.takeValueOrThrow().isEmpty()) {
                        tryEmit(result)
                    } else {
                        nextPage.invoke(result.value)
                        tryEmit(result)
                    }
                }
                else -> {
                    tryEmit(result)
                }
            }
        }
    }
}


fun ViewModel.launchPeriod(
    repeatMillis: Long,
    initMillis: Long,
    action: () -> Unit
) = viewModelScope.async {
    if (repeatMillis > 0) {
        delay(initMillis)
        while (isActive) {
            action()
            delay(repeatMillis)
        }
    } else {
        action()
    }
}

fun ViewModel.delay(
    timeInMillis: Long,
    action: () -> Unit
) = viewModelScope.async {
    delay(timeInMillis)
    action.invoke()
}

fun Fragment.launch(action: () -> Unit) {
    lifecycleScope.launch { action.invoke() }
}

fun <T> SharedFlow<T>.getCurrentData(): T? = replayCache.lastOrNull()
fun <T> MutableSharedFlow<T?>.clearData() = tryEmit(null)
