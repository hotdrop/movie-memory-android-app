package jp.hotdrop.moviememory.presentation.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.BuildConfig
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentSettingBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.service.Firebase
import timber.log.Timber
import javax.inject.Inject

class SettingFragment: Fragment() {

    private lateinit var binding: FragmentSettingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SettingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SettingViewModel::class.java)
    }

    @Inject
    lateinit var firebase: Firebase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.component?.fragment()?.inject(this) ?: run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
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

        binding.dataClearArea.setOnClickListener {
            context?.let { context ->
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
            // TODO OSSページ作る
            Snackbar.make(binding.snackbarArea, "ライセンス表記は未実装です。", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observe() {
        viewModel.movieCount.observe(viewLifecycleOwner, Observer {
            it?.run {
                binding.localCount.text = this.toString()
            }
        })
        viewModel.lastUpdateDateEpoch.observe(viewLifecycleOwner, Observer {
            it?.run {
                connectFirebase(this)
            }
        })
        viewModel.clearedMovies.observe(viewLifecycleOwner, Observer {
            it?.run {
                if (this) {
                    Snackbar.make(binding.snackbarArea, "データをクリアしました。", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.run {
                Snackbar.make(binding.snackbarArea, "エラーが発生しました。", Snackbar.LENGTH_SHORT).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun connectFirebase(lastUpdateDateEpoch: Long) {
        firebase.listenStatus(lastUpdateDateEpoch) { isUpdateData, status ->
            binding.remoteUpdate.let {
                if (isUpdateData) {
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    it.text = getString(R.string.setting_label_remote_update_have)
                } else {
                    it.text = getString(R.string.setting_label_remote_update_none)
                }
            }
            binding.remoteStatus.text = status
        }
    }

    companion object {
        fun newInstance(): SettingFragment = SettingFragment()
    }
}