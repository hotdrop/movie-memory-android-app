package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.*
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class NowPlayingMoviesViewModel @Inject constructor(
        private val repository: MovieRepository
): ViewModel() {

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCleared() {
        refreshNowPlayingMovies()
    }

    fun onRefresh() {
        refreshNowPlayingMovies()
    }

    private fun refreshNowPlayingMovies() {
        // ここで本当はRepositoryを呼ぶ
        val list = mutableListOf(
                Movie(1, "テスト1", "", Movie.Status.NowPlaying),
                Movie(2, "テスト2", "", Movie.Status.NowPlaying)
        )
        mutableMovies.postValue(list)
    }
}