package jp.hotdrop.moviememory.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.hotdrop.moviememory.di.component.FragmentComponent
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val fragmentComponent: FragmentComponent by lazy {
        (activity as BaseActivity).getComponent().plus()
    }

    fun getComponent(): FragmentComponent = fragmentComponent
}