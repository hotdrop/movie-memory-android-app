package jp.hotdrop.moviememory.presentation

import android.support.v4.app.Fragment
import jp.hotdrop.moviememory.di.FragmentComponent
import jp.hotdrop.moviememory.di.FragmentModule

abstract class BaseFragment: Fragment() {

    private val fragmentComponent by lazy {
        (activity as BaseActivity).getComponent().plus(FragmentModule())
    }

    fun getComponent(): FragmentComponent = fragmentComponent
}