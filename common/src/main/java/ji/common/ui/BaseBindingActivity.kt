package ji.common.ui

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.jibase.utils.ViewBindingUtils
import kotlin.reflect.KClass

open class BaseBindingActivity<BINDING : ViewBinding>(private val bindingClass: KClass<BINDING>) :
    BaseActivity() {
    open lateinit var binding: BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewBindingUtils.inflate(layoutInflater, bindingClass.java)
        setContentView(binding.root)
    }
}