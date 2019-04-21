package jp.hotdrop.moviememory.presentation.movie.edit

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieEditDetailBinding
import jp.hotdrop.moviememory.databinding.ItemCastBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Cast
import jp.hotdrop.moviememory.presentation.component.TextInputDatePickerDialog
import jp.hotdrop.moviememory.presentation.common.RecyclerViewAdapter
import jp.hotdrop.moviememory.presentation.component.CastDialog
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

class MovieEditDetailFragment: Fragment() {

    private lateinit var binding: FragmentMovieEditDetailBinding
    private var adapter: CastsAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: MovieEditViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.also {
            it.component.fragment().inject(this)
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(MovieEditViewModel::class.java)
        } ?: kotlin.run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieEditDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observe()

        arguments?.getLong(EXTRA_MOVIE_ID)?.let {
            viewModel?.find(it) ?: throw IllegalStateException("viewModel is null!!")
        }
    }

    private fun initView() {

        binding.playDateEditArea.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    TextInputDatePickerDialog.show(context, binding.playDateEditArea)
                }
            }
        }

        binding.addCastButton.setOnClickListener {
            CastDialog.Builder(requireContext())
                    .setOnPositiveListener { cast ->
                        adapter!!.add(cast)
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
                val newMovie = movie.copy(playingDate = newPlayingDate, casts = newCasts)

                viewModel?.save(newMovie) ?: throw IllegalStateException("viewModel is null!!")
            }
        }
    }

    private fun observe() {
        viewModel?.movie?.observe(this, Observer {
            it?.let { movie ->
                binding.movie = movie
                initViewForCastsEdit(movie.casts)
            }
        }) ?: throw IllegalStateException("viewModel is null!!")
    }

    private fun initViewForCastsEdit(casts: List<Cast>?) {

        binding.progressbar.isGone = true

        binding.castsRecyclerView.let { recyclerView ->
            recyclerView.layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = CastsAdapter(requireContext())
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

    class CastsAdapter(
            private val context: Context
    ): RecyclerViewAdapter<Cast, RecyclerViewAdapter.BindingHolder<ItemCastBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemCastBinding> =
                BindingHolder(parent, R.layout.item_cast)

        override fun onBindViewHolder(holder: BindingHolder<ItemCastBinding>, position: Int) {
            val holderBinding = holder.binding
            holderBinding?.let { binding ->
                val cast = getItem(position)
                binding.castName.text = cast.actor?.let { "${cast.name}:${cast.actor}" } ?: cast.name
                binding.iconCastDelete.isVisible = true

                binding.iconCastDelete.setOnClickListener {
                    val message = context.getString(R.string.cast_delete_text, cast.name)
                    // proguardを有効にしたらsetPositiveButtonのラムダで「can't find referenced class」エラーが発生した。
                    // proguardに除外設定書くかラムダやめるか迷ったが、とりあえずいちいち書いてみることにした。
                    val positiveOnClickListener = (object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            remove(cast)
                            dialog?.dismiss()
                        }
                    })
                    val negativeOnClickListener = (object: DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }
                    })
                    AlertDialog.Builder(context)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, positiveOnClickListener)
                            .setNegativeButton(android.R.string.cancel, negativeOnClickListener)
                            .setCancelable(true)
                            .show()
                }

                binding.castLayout.setOnClickListener {
                    CastDialog.Builder(context)
                            .setOnPositiveListener { newCast ->
                                super.update(cast, newCast)
                            }.showEdit(cast)
                }
            }
        }
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(movieId: Long): MovieEditDetailFragment = MovieEditDetailFragment().apply {
            arguments = Bundle().apply { putLong(EXTRA_MOVIE_ID, movieId) }
        }
    }
}