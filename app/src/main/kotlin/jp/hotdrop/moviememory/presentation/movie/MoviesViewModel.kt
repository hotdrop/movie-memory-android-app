package jp.hotdrop.moviememory.presentation.movie

import android.arch.lifecycle.ViewModel
import jp.hotdrop.moviememory.data.repository.MovieRepository
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
        private val repository: MovieRepository
): ViewModel() {

}