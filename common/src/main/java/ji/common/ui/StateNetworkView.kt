package ji.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.forEach
import com.jibase.extensions.gone
import com.jibase.extensions.visible
import ji.common.R
import ji.common.databinding.LayoutErrorNetworkBinding

class StateNetworkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    def: Int = 0
) : FrameLayout(context, attrs, def) {


    enum class NetworkType {
        LOADING,
        ERROR,
        SUCCESS,
        EMPTY
    }

    // true to show progress, false to show refresh
    private var onClickListener: ((NetworkType) -> Unit)? = null

    var currentState = NetworkType.LOADING

    private val binding: LayoutErrorNetworkBinding

    init {

        binding = LayoutErrorNetworkBinding.inflate(LayoutInflater.from(context), this)


        // init arts
        attrs?.also {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.StateNetworkView, 0, 0)

            val enablePullToRefresh = typedArray.getBoolean(
                R.styleable.StateNetworkView_state_enable_pull_to_refresh,
                true
            )

            binding.swipeRefreshLayout.isEnabled = enablePullToRefresh

            val layoutProgressRes =
                typedArray.getResourceId(R.styleable.StateNetworkView_state_progress_layout, -1)
            replaceViewIfNeed(R.string.tag_progress, layoutProgressRes)

            val layoutErrorRes =
                typedArray.getResourceId(R.styleable.StateNetworkView_state_error_layout, -1)
            replaceViewIfNeed(R.string.tag_error, layoutErrorRes)

            val layoutEmptyRes =
                typedArray.getResourceId(R.styleable.StateNetworkView_state_empty_layout, -1)

            if (layoutEmptyRes > 0) {
                replaceViewIfNeed(R.string.tag_empty, layoutEmptyRes)
            } else {
                findTag(R.string.tag_empty)?.also { view ->
                    val icon = typedArray.getDrawable(R.styleable.StateNetworkView_state_empty_icon)
                    if (icon != null) {
                        view.findViewById<ImageView>(R.id.imageEmpty).apply {
                            visible()
                            setImageDrawable(icon)
                        }
                    }

                    val text = typedArray.getString(R.styleable.StateNetworkView_state_empty_text)
                    if (text?.isNotEmpty() == true) {
                        view.findViewById<TextView>(R.id.tvEmpty).text = text
                    }
                }
            }

            typedArray.recycle()
        }

        findTag(R.string.tag_error)?.findViewWithTag<View>(context.getString(R.string.tag_button))
            ?.setOnClickListener {
                if (binding.swipeRefreshLayout.isRefreshing || currentState == NetworkType.LOADING) return@setOnClickListener
                updateUI(NetworkType.LOADING)
                onClickListener?.invoke(NetworkType.ERROR)
            }

        findTag(R.string.tag_empty)?.findViewWithTag<View>(context.getString(R.string.tag_button))
            ?.setOnClickListener {
                if (binding.swipeRefreshLayout.isRefreshing || currentState == NetworkType.LOADING) return@setOnClickListener
                updateUI(NetworkType.LOADING)
                onClickListener?.invoke(NetworkType.EMPTY)
            }

        binding.swipeRefreshLayout.setOnRefreshListener {
            val temp = currentState
            updateUI(NetworkType.LOADING)
            onClickListener?.invoke(temp)
        }
    }

    fun setCallBack(onClickListener: ((NetworkType) -> Unit)?): StateNetworkView {
        this.onClickListener = onClickListener
        return this
    }

    fun updateUI(networkType: NetworkType) {
        if (currentState == networkType) return
        currentState = networkType
        when (networkType) {
            NetworkType.LOADING -> {
                updateViewState()
                if (!binding.swipeRefreshLayout.isRefreshing) findTag(R.string.tag_progress)?.visible()
            }

            NetworkType.ERROR -> {
                updateViewState()
                findTag(R.string.tag_error)?.visible()
                binding.swipeRefreshLayout.isRefreshing = false
            }

            NetworkType.SUCCESS -> {
                updateViewState(hideContent = false)
                binding.swipeRefreshLayout.isRefreshing = false
            }

            NetworkType.EMPTY -> {
                updateViewState()
                findTag(R.string.tag_empty)?.visible()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun updateViewState(hideTag: Boolean = true, hideContent: Boolean = true) {
        binding.flState.forEach {
            when {
                hideTag && it.tag != null -> it.gone()
                hideContent && it.tag == null -> it.gone()
                else -> it.visible()
            }
        }
    }

    private fun findTag(@StringRes tagRes: Int): View? {
        return binding.flState.findViewWithTag(context.getString(tagRes))
    }

    private fun replaceViewIfNeed(@StringRes tagRes: Int, @LayoutRes layoutId: Int) {
        if (layoutId <= 0) return
        findTag(tagRes)?.also {
            binding.flState.removeView(it)
        }

        val layout = LayoutInflater.from(context).inflate(layoutId, null)
        layout.tag = context.getString(tagRes)
        binding.flState.addView(layout)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child?.javaClass?.name == "androidx.swiperefreshlayout.widget.SwipeRefreshLayout") {
            super.addView(child, index, params)
        } else {
            binding.flState.addView(child)
        }
    }
}