package ji.common.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ji.common.R
import ji.common.flow.*
import ji.common.ui.confirmdialog.ConfigText
import ji.common.ui.confirmdialog.InfText
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
open class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    private var progressDialog: ProgressDialog? = null

    open fun <T> observeFlow(
        stateFlow: StateFlow<ResultWrapper<T>>,
        successHandler: SuccessHandler<T>,
        errorHandler: ErrorHandler = {
            loadingHandler.invoke(false)
            showErrorMessage(it)
        },
        loadingHandler: LoadingHandler = { isShow ->
            if (isShow) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
            true
        }
    ) {
        lifecycleScope.collect(
            flow = stateFlow,
            successHandler = {
                loadingHandler.invoke(false)
                successHandler.invoke(it)
            },

            errorHandler = errorHandler,
            loadingHandler = loadingHandler
        )
    }

    open fun <T> pagingFlow(
        stateFlow: StateFlow<ResultWrapper<T>>,
        successHandler: SuccessHandler<T>,
        errorHandler: ErrorHandler = {
            loadingHandler.invoke(false)
        },
        loadingHandler: LoadingHandler = { isShow ->
            if (isShow) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
            true
        },
        initHandler: InitHandler? = null
    ) {
        lifecycleScope.pagingCollect(
            stateFlow,
            successHandler = { result ->
                if (result != null) {
                    successHandler.invoke(result)
                }
            },

            errorHandler = errorHandler,

            loadingHandler = { show ->
                if (loadingHandler.invoke(show)) {
                    if (show) {
                        showLoadingDialog()
                    } else {
                        hideLoadingDialog()
                    }
                }

                true
            },

            initHandler = initHandler,
        )
    }

    override fun onDestroyView() {
        progressDialog?.forceDismiss()
        super.onDestroyView()
    }


    open fun showLoadingDialog() {
        progressDialog = progressDialog ?: ProgressDialog()
        progressDialog?.show(this)
    }

    open fun hideLoadingDialog() {
        progressDialog?.safeDismiss()
    }

    open fun showErrorMessage(throwable: Throwable?) {
        ConfirmDialog.newBuilder()
            .setTitle(ConfigText().setText(InfText.Resource(R.string.confirm_title)))
            .setMessage(
                ConfigText().setText(
                    InfText.Text(
                        throwable?.message ?: getString(R.string.unknow_error)
                    )
                )
            )
            .build()
            .show(childFragmentManager)
    }
}