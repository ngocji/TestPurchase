package ji.common.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

fun <T> FragmentActivity.collect(flow: Flow<T>, action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest(action)
    }
}

fun <T> FragmentActivity.collectOne(
    flow: MutableStateFlow<T?>,
    action: suspend (value: T) -> Unit
) {
    lifecycleScope.launchWhenStarted {
        flow.collect {
            it ?: return@collect
            action.invoke(it)
            flow.clear()
        }
    }
}

fun <T> FragmentActivity.collectNotNull(
    flow: MutableStateFlow<T?>,
    action: suspend (value: T) -> Unit
) {
    lifecycleScope.launchWhenStarted {
        flow.collect {
            it ?: return@collect
            action.invoke(it)
        }
    }
}


fun <T> FragmentActivity.collectWhenResume(flow: Flow<T>, action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenResumed {
        flow.collectLatest(action)
    }
}

fun <T> FragmentActivity.run(stateFlow: StateFlow<T>) {
    lifecycleScope.launchWhenStarted {
        stateFlow.collect {}
    }
}


fun <T> Fragment.collect(flow: Flow<T>, action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest(action)
    }
}

fun <T> Fragment.collectOne(flow: MutableStateFlow<T?>, action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collect {
            it ?: return@collect
            action.invoke(it)
            flow.clear()
        }
    }
}

fun <T> Fragment.collectNotNull(flow: MutableStateFlow<T?>, action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collect {
            it ?: return@collect
            action.invoke(it)
        }
    }
}


fun <T> Fragment.collectWhenResume(flow: Flow<T>, action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenResumed {
        flow.collectLatest(action)
    }
}

fun <T> Fragment.run(stateFlow: StateFlow<T>) {
    lifecycleScope.launchWhenStarted {
        stateFlow.collect {}
    }
}
