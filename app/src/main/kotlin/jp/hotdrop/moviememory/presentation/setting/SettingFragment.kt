package jp.hotdrop.moviememory.presentation.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.hotdrop.moviememory.databinding.FragmentSettingBinding
import jp.hotdrop.moviememory.presentation.BaseFragment

class SettingFragment: BaseFragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observe()
    }

    private fun initView() {

    }

    private fun observe() {

    }

    companion object {
        fun newInstance(): SettingFragment = SettingFragment()
    }
}