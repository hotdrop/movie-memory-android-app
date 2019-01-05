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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieEditDetailBinding
import jp.hotdrop.moviememory.databinding.ItemCastBinding
import jp.hotdrop.moviememory.presentation.BaseFragment
import jp.hotdrop.moviememory.presentation.component.TextInputDatePickerDialog
import jp.hotdrop.moviememory.presentation.component.TextInputDialog
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import org.threeten.bp.LocalDate
import java.lang.IllegalStateException

class MovieEditDetailFragment: BaseFragment() {

    private lateinit var binding: FragmentMovieEditDetailBinding
    private var adapter: CastsAdapter? = null
    private var viewModel: MovieEditViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
        activity?.run {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieEditViewModel::class.java)
        } ?: throw IllegalStateException("viewModel is null!!")
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
                if (hasFocus && context != null) {
                    TextInputDatePickerDialog.show(context, binding.playDateEditArea)
                }
            }
        }

        binding.addCastButton.setOnClickListener {
            if (context != null && adapter != null) {
                TextInputDialog.Builder(context!!)
                        .setTitle(R.string.dialog_title_cast_add)
                        .setTextHint(R.string.cast_text_hint)
                        .setOnPositiveListener { castName ->
                            adapter!!.add(castName)
                        }.show()
            }
        }

        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->

                val newPlayingDateText = binding.playDateEditArea.text.toString()
                val newPlayingDate = if (newPlayingDateText.isNotEmpty()) {
                    LocalDate.parse(newPlayingDateText)
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

    private fun initViewForCastsEdit(casts: List<String>?) {

        binding.progressbar.isGone = true

        if (casts == null || casts.isEmpty()) {
            binding.castsEmptyMessage.isVisible = true
            return
        }

        binding.castsRecyclerView.let { recyclerView ->
            recyclerView.layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = CastsAdapter().apply { addAll(casts) }
            recyclerView.adapter = adapter
            recyclerView.isVisible = true
        }
    }

    inner class CastsAdapter: RecyclerViewAdapter<String, RecyclerViewAdapter.BindingHolder<ItemCastBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemCastBinding> =
                BindingHolder(parent, R.layout.item_cast)

        override fun onBindViewHolder(holder: BindingHolder<ItemCastBinding>, position: Int) {
            val holderBinding = holder.binding
            holderBinding?.let { binding ->
                val castName = getItem(position)
                binding.castName.text = castName
                binding.iconCastDelete.isVisible = true

                binding.iconCastDelete.setOnClickListener {
                    context?.let { context ->
                        val message = context.getString(R.string.cast_delete_text, castName)
                        AlertDialog.Builder(context)
                                .setMessage(message)
                                .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                                    super.remove(castName)
                                    dialogInterface.dismiss()
                                }.setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                                    dialogInterface.dismiss()
                                }.setCancelable(true)
                                .show()
                    }
                }

                binding.castLayout.setOnClickListener {
                    context?.let { context ->
                        TextInputDialog.Builder(context)
                                .setTitle(R.string.dialog_title_cast_update)
                                .setTextHint(R.string.cast_text_hint)
                                .setText(castName)
                                .setOnPositiveListener { updatedCastName ->
                                    super.update(castName, updatedCastName)
                                }.show()
                    }
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