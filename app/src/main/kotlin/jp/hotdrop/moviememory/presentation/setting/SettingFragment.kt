package jp.hotdrop.moviememory.presentation.setting

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.BuildConfig
import jp.hotdrop.moviememory.R
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

        activity?.actionBar?.setTitle(R.string.title_setting)

        binding.langArea.setOnClickListener { view ->
            PopupMenu(context, view).run {
                menuInflater.inflate(R.menu.popup_language, this.menu)
                setOnMenuItemClickListener {
                    Snackbar.make(binding.snackbarArea, "${it.title} を選択しました。未実装です。", Snackbar.LENGTH_SHORT).show()
                    true
                }
                show()
            }
        }

        binding.dataClearArea.setOnClickListener {
            context?.let { context ->
                AlertDialog.Builder(context)
                        .setTitle(R.string.setting_label_data_clear)
                        .setMessage(R.string.dialog_data_clear_message)
                        .setPositiveButton(resources.getString(android.R.string.ok)) { _, which ->
                            Snackbar.make(binding.snackbarArea, "データクリアします。未実装です。", Snackbar.LENGTH_SHORT).show()
                        }
                        .setNegativeButton(resources.getString(android.R.string.cancel), null)
                        .show()
            }
        }

        binding.appVersion.text = BuildConfig.VERSION_NAME

        binding.licenseArea.setOnClickListener {
            Snackbar.make(binding.snackbarArea, "ライセンス表記は未実装です。", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observe() {
    }

    companion object {
        fun newInstance(): SettingFragment = SettingFragment()
    }
}