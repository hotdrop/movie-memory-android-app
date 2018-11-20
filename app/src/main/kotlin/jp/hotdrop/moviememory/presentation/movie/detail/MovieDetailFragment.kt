package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.appbar.AppBarLayout
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieDetailBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.BaseFragment
import javax.inject.Inject

class MovieDetailFragment: BaseFragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var activity: MovieDetailActivity

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders.of(activity, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)

        (getActivity() as MovieDetailActivity).let {
            activity = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
    }

    private fun observe() {
        viewModel.movie?.observe(this, Observer {
            it?.let {
                binding.movie = it
            }
        })
    }

    private fun initView() {

        activity.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }
        }

        // Droidkaigi2018のCoordinatorLayoutの動きが素晴らしかったので真似ました。
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            binding.toolbarTextColorFactor = factor
        })

        // TODO このfabは単なるお気に入りにする予定
        binding.fab.setOnClickListener { view ->
            TODO()
        }

        binding.movieUrlLink.setOnClickListener {
            binding.movie?.let {
                startToWebLink(it.imageUrl)
            }
        }

        binding.officialUrlLink.setOnClickListener {
            binding.movie?.let {
                startToWebLink(it.url)
            }
        }

        binding.movieEditImage.setOnClickListener {
            binding.movie?.let {
                activity.showEditFragment()
            }
        }
    }

    private fun startToWebLink(url: String?) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(activity, getString(R.string.movie_link_tap_non_url), Toast.LENGTH_SHORT).show()
        } else {
            activity.startBrowser(url)
        }
    }

    companion object {
        fun newInstance() = MovieDetailFragment()
    }
}