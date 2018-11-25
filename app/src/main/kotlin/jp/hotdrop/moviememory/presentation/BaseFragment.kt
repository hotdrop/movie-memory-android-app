package jp.hotdrop.moviememory.presentation

import jp.hotdrop.moviememory.di.component.FragmentComponent
import jp.hotdrop.moviememory.di.module.FragmentModule

abstract class BaseFragment: androidx.fragment.app.Fragment() {

    private val fragmentComponent: FragmentComponent by lazy {
        (activity as BaseActivity).getComponent().plus(FragmentModule())
    }

    fun getComponent(): FragmentComponent = fragmentComponent

}