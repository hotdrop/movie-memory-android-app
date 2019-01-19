package jp.hotdrop.moviememory.presentation.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentCategoryBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.Category
import javax.inject.Inject

class CategoryFragment: Fragment() {

    private lateinit var binding: FragmentCategoryBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity?.also {
            it.component.fragment().inject(this)
        } ?: onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false  )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observe()
    }

    private fun observe() {
        viewModel.categories.observe(this, Observer {
            it?.let { categories ->
                initChipCategories(categories)
            }
        })
        viewModel.streamCategories?.observe(this, Observer {
            it?.let { categories ->
                initChipCategories(categories)
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initView() {
        binding.fab.setOnClickListener {
            // TODO 追加ボタン
            Snackbar.make(binding.snackbarArea, "追加は未実装です。", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()
        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_entry, binding.chipGroupCategories, false) as Chip).apply {
                text = category.name
                setOnClickListener {
                    // TODO 名前変更のダイアログ表示
                    Snackbar.make(binding.snackbarArea, "更新は未実装です。", Snackbar.LENGTH_SHORT).show()
                }
                setOnCloseIconClickListener {
                    // TODO 削除確認ダイアログ表示
                    Snackbar.make(binding.snackbarArea, "削除は未実装です。", Snackbar.LENGTH_SHORT).show()
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    companion object {
        fun newInstance(): CategoryFragment = CategoryFragment()
    }
}