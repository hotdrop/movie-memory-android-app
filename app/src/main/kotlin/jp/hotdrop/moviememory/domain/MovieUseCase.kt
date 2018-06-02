package jp.hotdrop.moviememory.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MovieUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    /**
     * 公開日からいつまでがNowPlayingかはここで判定すべき。
     * そもそもnowPlayingとかComingSoonは、APIで取得したものではなくアプリ側で勝手に決めて表示するため
     * その公開日の判断などはここでやるべき。Repositoryは単純に指定の条件でデータを持ってくるだけの方がいい。
     */
    fun nowPlayingMovies(offset: Int): Flowable<List<Movie>> =
            repository.moviesByPlayingDate(offset)

    fun movie(id: Int): Single<Movie> =
            repository.movie(id)
                    .subscribeOn(Schedulers.io())

    fun refreshNowPlayingMovies(offset: Int): Completable =
        repository.refreshNowPlayingMovies(offset)
                .subscribeOn(Schedulers.io())

    fun loadNowPlayingMovies(index: Int, offset: Int): Completable =
            repository.loadNowPlayingMovies(index, offset)
                    .subscribeOn(Schedulers.io())
}