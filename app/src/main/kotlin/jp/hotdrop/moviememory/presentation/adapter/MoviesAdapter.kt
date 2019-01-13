package jp.hotdrop.moviememory.presentation.adapter

import android.view.ViewGroup
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.common.RecyclerViewAdapter

class MoviesAdapter constructor(
        private val onMovieClickListener: (binding: ItemMovieBinding, movie: Movie) -> Unit
): RecyclerViewAdapter<Movie, RecyclerViewAdapter.BindingHolder<ItemMovieBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemMovieBinding> =
            BindingHolder(parent, R.layout.item_movie)

    override fun onBindViewHolder(holder: BindingHolder<ItemMovieBinding>, position: Int) {
        val binding = holder.binding
        binding?.let { itemBinding ->
            val movie = getItem(position)
            itemBinding.movie = movie
            itemBinding.movieLayout.setOnClickListener {
                onMovieClickListener(itemBinding, movie)
            }
        }
    }

    fun refresh(movie: Movie) {
        getItemPosition(movie)?.let { index ->
            replace(index, movie)
        }
    }
}