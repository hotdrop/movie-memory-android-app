package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.appbar.AppBarLayout
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieDetailBinding
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

        binding.trailerUrlLink.setOnClickListener {
            binding.movie?.trailerMovieUrl?.run {
                startToWebLink(this)
            }
        }

        binding.officialUrlLink.setOnClickListener {
            binding.movie?.officialUrl?.run {
                startToWebLink(this)
            }
        }

        binding.editFab.setOnClickListener {
            activity.showEditFragment()
        }
    }

    private fun startToWebLink(url: String) {
        if (url.startsWith("http")) {
            activity.startBrowser(url)
        }
    }

    companion object {
        fun newInstance() = MovieDetailFragment()
    }
}