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
import jp.hotdrop.moviememory.presentation.component.CategoryDialog
import timber.log.Timber
import javax.inject.Inject

class CategoryFragment: Fragment() {

    private lateinit var binding: FragmentCategoryBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CategoryViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
    }

    override fun onAttach(context: Context) {
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
        viewModel.success.observe(this, Observer {
            it?.let { type ->
                onSuccessSnackbar(type)
                viewModel.refresh()
            }
        })
        viewModel.error.observe(this, Observer {
            it?.let { error ->
                Snackbar.make(binding.snackbarArea, error.getMessage(), Snackbar.LENGTH_LONG).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initView() {
        binding.fab.setOnClickListener {
            viewModel.categories.value?.let { categories ->
                showAddDialog(categories)
            }
        }
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()
        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_entry, binding.chipGroupCategories, false) as Chip).apply {
                this.text = getString(R.string.category_name_text, category.name, category.registerCount)
                this.setOnClickListener {
                    showEditDialog(category, categories)
                }
                this.setOnCloseIconClickListener {
                    showDeleteDialog(category)
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun showAddDialog(categories: List<Category>) {
        CategoryDialog.Builder(requireContext())
                .setCategoriesForCheckDuplicate(categories)
                .setOnPositiveListener { newCategory, _ ->
                    viewModel.add(newCategory)
                }.showAdd()
    }

    private fun showEditDialog(category: Category, categories: List<Category>) {
        CategoryDialog.Builder(requireContext())
                .setCategoriesForCheckDuplicate(categories)
                .setOnPositiveListener { newCategory, isIntegrate ->
                    if (isIntegrate) {
                        Timber.d("重複があるためカテゴリーを統合します。統合元:${category.id}, ${category.name} 統合先:${newCategory.id} ${newCategory.name}")
                        viewModel.integrate(category, newCategory)
                    } else {
                        Timber.d("普通にカテゴリー名を更新します。")
                        viewModel.update(newCategory)
                    }
                }.showEdit(category)
    }

    private fun showDeleteDialog(category: Category) {
        CategoryDialog.Builder(requireContext())
                .setMessage(R.string.category_delete_dialog_message, category.name)
                .setOnPositiveListener { _, _ ->
                    viewModel.delete(category)
                }.showDelete()
    }

    private fun onSuccessSnackbar(type: CategoryViewModel.Companion.OperationType) {
        when (type) {
            CategoryViewModel.Companion.OperationType.ADD -> {
                Snackbar.make(binding.snackbarArea, R.string.category_add_success_message, Snackbar.LENGTH_SHORT).show()
            }
            CategoryViewModel.Companion.OperationType.UPDATE -> {
                Snackbar.make(binding.snackbarArea, R.string.category_update_success_message, Snackbar.LENGTH_SHORT).show()
            }
            CategoryViewModel.Companion.OperationType.DELETE -> {
                Snackbar.make(binding.snackbarArea, R.string.category_delete_success_message, Snackbar.LENGTH_SHORT).show()
            }
            CategoryViewModel.Companion.OperationType.REPLACE -> {
                Snackbar.make(binding.snackbarArea, R.string.category_integrate_success_message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(): CategoryFragment = CategoryFragment()
    }
}