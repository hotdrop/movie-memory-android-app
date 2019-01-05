package jp.hotdrop.moviememory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Reusable
import javax.inject.Inject
import javax.inject.Provider

@Reusable
class ViewModelFactory @Inject constructor(
        private val creatorMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        var vmProvider: Provider<out ViewModel>? = creatorMap[modelClass]
        if (vmProvider == null) {
            for((key, value) in creatorMap) {
                if (modelClass.isAssignableFrom(key)) {
                    vmProvider = value
                    break
                }
            }
        }
        vmProvider ?: throw IllegalArgumentException("unknown model class $modelClass")
        @Suppress("UNCHECKED_CAST")
        return vmProvider.get() as T
    }
}