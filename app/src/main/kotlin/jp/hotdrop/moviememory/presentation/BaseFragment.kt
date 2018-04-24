package jp.hotdrop.moviememory.presentation

import android.support.v4.app.Fragment
import jp.hotdrop.moviememory.di.component.FragmentComponent
import jp.hotdrop.moviememory.di.module.FragmentModule

abstract class BaseFragment: Fragment() {

    private val fragmentComponent by lazy {
        (activity as BaseActivity).getComponent().plus(FragmentModule())
    }

    fun getComponent(): FragmentComponent = fragmentComponent

}