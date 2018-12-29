package jp.hotdrop.moviememory.presentation.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.BuildConfig
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentSettingBinding
import jp.hotdrop.moviememory.presentation.BaseFragment
import javax.inject.Inject

class SettingFragment: BaseFragment() {

    private lateinit var binding: FragmentSettingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SettingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SettingViewModel::class.java)
    }

    override fun onAttach(context: Context) {
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
        binding.langArea.setOnClickListener { view ->
            PopupMenu(context, view).run {
                menuInflater.inflate(R.menu.popup_language, this.menu)
                setOnMenuItemClickListener {
                    // TODO 言語設定。これこのアプリに必要ないがLocale変更ってやつをやっておく
                    Snackbar.make(binding.snackbarArea, "${it.title} を選択しました。未実装です。", Snackbar.LENGTH_SHORT).show()
                    true
                }
                show()
            }
        }

        binding.dataClearArea.setOnClickListener {
            context?.let { context ->
                // TODO このデータクリア、意味なさそうな感じになってきたので見直す必要がある
                AlertDialog.Builder(context)
                        .setTitle(R.string.setting_label_data_clear)
                        .setMessage(R.string.dialog_data_clear_message)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            viewModel.clearMovies()
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
            }
        }

        binding.appVersion.text = BuildConfig.VERSION_NAME

        binding.licenseArea.setOnClickListener {
            // TODO OSSページ
            Snackbar.make(binding.snackbarArea, "ライセンス表記は未実装です。", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observe() {
        viewModel.clearedMovies.observe(this, Observer {
            it?.let { success ->
                if (success) {
                    Snackbar.make(binding.snackbarArea, "データをクリアしました。", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.snackbarArea, "データのクリアに失敗しました。何かがおかしいです。", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    companion object {
        fun newInstance(): SettingFragment = SettingFragment()
    }
}