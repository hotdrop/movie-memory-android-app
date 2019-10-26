package jp.hotdrop.moviememory.presentation.movie.edit

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieEditBinding
import jp.hotdrop.moviememory.databinding.ItemCastBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Cast
import jp.hotdrop.moviememory.presentation.BaseActivity
import jp.hotdrop.moviememory.presentation.common.adapter.RecyclerViewAdapter
import jp.hotdrop.moviememory.presentation.common.dialog.CastDialog
import jp.hotdrop.moviememory.presentation.common.dialog.TextInputDatePickerDialog
import javax.inject.Inject

class MovieEditActivity: BaseActivity() {

    private lateinit var binding: ActivityMovieEditBinding
    private var adapter: CastsAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieEditViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_edit)

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

        binding.playDateEditArea.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    TextInputDatePickerDialog.show(context, binding.playDateEditArea)
                }
            }
        }

        binding.addCastButton.setOnClickListener {
            CastDialog.Builder(this)
                    .setOnPositiveListener { cast ->
                        adapter!!.add(cast)
                        refreshCasts()
                    }.showAdd()
        }

        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->

                val newPlayingDateText = binding.playDateEditArea.text.toString()
                val newPlayingDate = if (newPlayingDateText.isNotEmpty()) {
                    AppDate(dateStr = newPlayingDateText)
                } else {
                    movie.playingDate
                }
                val newCasts = adapter?.getAll() ?: movie.casts

                val newMovie = movie.copy(playingDate = newPlayingDate, casts = newCasts, makeYear = movie.makeYear)
                viewModel.save(newMovie)
            }
        }
    }

    private fun observe() {
        viewModel.movie.observe(this, Observer {
            it?.run {
                binding.movie = this
                initViewForCastsEdit(this.casts)
            }
        })
        viewModel.saveSuccess.observe(this, Observer {
            it?.run {
                if (this) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
        viewModel.error.observe(this, Observer {
            it?.run {
                Snackbar.make(binding.snackbarArea, "保存に失敗しました。", Snackbar.LENGTH_LONG).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initViewForCastsEdit(casts: List<Cast>?) {

        binding.progressbar.isGone = true

        binding.castsRecyclerView.let { recyclerView ->
            recyclerView.layoutManager = FlexboxLayoutManager(this).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = CastsAdapter()
            recyclerView.adapter = adapter

        }

        if (casts == null || casts.isEmpty()) {
            binding.castsEmptyMessage.isVisible = true
            return
        } else {
            adapter!!.addAll(casts)
            binding.castsRecyclerView.isVisible = true
        }
    }

    private fun refreshCasts() {
        val casts = adapter?.getAll()
        if (casts == null || casts.isEmpty()) {
            binding.castsRecyclerView.isGone = true
            binding.castsEmptyMessage.isVisible = true
        } else {
            binding.castsRecyclerView.isVisible = true
            binding.castsEmptyMessage.isGone = true
        }
    }

    inner class CastsAdapter: RecyclerViewAdapter<Cast, RecyclerViewAdapter.BindingHolder<ItemCastBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemCastBinding> =
                BindingHolder(parent, R.layout.item_cast)

        override fun onBindViewHolder(holder: BindingHolder<ItemCastBinding>, position: Int) {
            val holderBinding = holder.binding
            holderBinding?.let { binding ->
                val cast = getItem(position)
                binding.castName.text = cast.charName?.let { "${cast.charName}(${cast.actor})" } ?: cast.actor
                binding.iconCastDelete.isVisible = true

                binding.iconCastDelete.setOnClickListener {
                    val message = getString(R.string.movie_edit_cast_delete_text, cast.charName)
                    // proguardを有効にしたらsetPositiveButtonのラムダで「can't find referenced class」エラーが発生した。
                    // proguardに除外設定書くかラムダやめるか迷ったが、とりあえずいちいち書いてみることにした。
                    val positiveOnClickListener = (object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            remove(cast)
                            refreshCasts()
                            dialog?.dismiss()
                        }
                    })
                    val negativeOnClickListener = (object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }
                    })
                    AlertDialog.Builder(this@MovieEditActivity)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, positiveOnClickListener)
                            .setNegativeButton(android.R.string.cancel, negativeOnClickListener)
                            .setCancelable(true)
                            .show()
                }

                binding.castLayout.setOnClickListener {
                    CastDialog.Builder(this@MovieEditActivity)
                            .setOnPositiveListener { newCast ->
                                super.update(cast, newCast)
                            }.showEdit(cast)
                }
            }
        }
    }

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"
        fun startForResult(activity: Activity, movieId: Long, requestCode: Int) =
                activity.startActivityForResult(Intent(activity, MovieEditActivity::class.java)
                        .apply {
                            putExtra(EXTRA_TAG, movieId)
                        }, requestCode)
    }
}