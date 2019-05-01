package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityAttachCategoryBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.presentation.BaseActivity
import javax.inject.Inject

class CategoryAttachActivity: BaseActivity() {

    private lateinit var binding: ActivityAttachCategoryBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CategoryAttachViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CategoryAttachViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_attach_category)

        val movieId = intent.getLongExtra(EXTRA_TAG, -1)
        viewModel.find(movieId)

        initView()
        observe()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }

    private fun observe() {
        viewModel.movie.observe(this, Observer {
            it?.run {
                viewModel.findCategories()
            }
        })

        viewModel.categories.observe(this, Observer {
            it?.run {
                initChipCategories(this)
                binding.progress.isGone = true
                binding.contentArea.isVisible = true
            }
        })

        viewModel.saveSuccess.observe(this, Observer {
            it?.let { success ->
                if (success) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()

        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_filter, binding.chipGroupCategories, false) as Chip)
                    .apply {
                        val categoryName = category.name
                        text = categoryName
                        setOnClickListener {
                            viewModel.save(categoryName)
                            (0 until binding.chipGroupCategories.childCount).forEach { idx ->
                                val chip = binding.chipGroupCategories[idx] as Chip
                                if (chip.text != categoryName) {
                                    chip.isChecked = false
                                }
                            }
                        }
                        if (categoryName == viewModel.movie.value!!.categoryName()) {
                            this.isChecked = true
                        }
                    }
            binding.chipGroupCategories.addView(chip)
        }
    }

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"
        fun startForResult(activity: Activity, movieId: Long, requestCode: Int) =
                activity.startActivityForResult(Intent(activity, CategoryAttachActivity::class.java)
                        .apply {
                            putExtra(EXTRA_TAG, movieId)
                        }, requestCode)
    }
}