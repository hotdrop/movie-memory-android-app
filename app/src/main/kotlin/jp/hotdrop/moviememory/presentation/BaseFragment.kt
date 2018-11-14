package jp.hotdrop.moviememory.presentation

import androidx.fragment.app.Fragment
import jp.hotdrop.moviememory.di.component.FragmentComponent
import jp.hotdrop.moviememory.di.module.FragmentModule

abstract class BaseFragment: androidx.fragment.app.Fragment() {

    private val fragmentComponent by lazy {
        (activity as BaseActivity).getComponent().plus(FragmentModule())
    }

    fun getComponent(): FragmentComponent = fragmentComponent

}