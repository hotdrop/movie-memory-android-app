package jp.hotdrop.moviememory.data.repository

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import jp.hotdrop.moviememory.data.local.database.CategoryDatabase
import jp.hotdrop.moviememory.data.local.database.MovieDatabase
import jp.hotdrop.moviememory.data.local.database.MovieNoteDatabase
import jp.hotdrop.moviememory.data.local.database.SuggestionDatabase
import jp.hotdrop.moviememory.data.local.entity.*
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

@Reusable
class SearchRepository @Inject constructor(
        private val movieDatabase: MovieDatabase,
        private val movieNoteDatabase: MovieNoteDatabase,
        private val categoryDatabase: CategoryDatabase,
        private val suggestionDatabase: SuggestionDatabase
) {

    fun suggestion(): Flowable<List<Suggestion>> =
            suggestionDatabase.suggestion()
                    .map {
                        it.map { entity -> entity.toSuggestion() }
                    }

    fun save(suggestion: Suggestion): Completable =
            Completable.create { emitter ->
                suggestionDatabase.save(suggestion.toEntity())
                emitter.onComplete()
            }

    fun deleteSuggestion(): Completable =
            Completable.create { emitter ->
                suggestionDatabase.delete()
                emitter.onComplete()
            }

    /**
     * movieを全て取得しinfoをくっつけた状態でkeywordのフィルターをかける方が楽だったが
     * movieの総量が比較的大きく、メモによるキーワード検索の結果数が比較的小さいと仮定すると
     * movieから検索＋infoから逆引き検索 で得られた2つの結果をマージした方がメモリ効率は良いと判断した。
     * 最初の頃はmovieがたかだか数百件で誤差の範囲なるだろうが多分この仮定は正しい。
     */
    fun findMovies(keyword: String): Single<List<Movie>> {
        return Single.zip<List<Movie>, List<Movie>, List<Movie>>(
                findMoviesFirstSearchFromMain(keyword),
                findMoviesFirstSearchFromLocalInfo(keyword),
                BiFunction { t1, t2 ->
                    t1.plus(t2).distinctBy { it.id }
                }
        )
    }

    fun findMovies(category: Category): Single<List<Movie>> {
        category.id?.let {
            return movieDatabase.findMovies(category.id)
                    .map { movieEntities ->
                        Timber.d("検索結果は ${movieEntities.size} ")
                        movieEntities.map {
                            entity -> entityToMovieWithLocalInfo(entity)
                        }
                    }
        } ?: throw IllegalStateException("Searchで映画情報をcategoryで取得しようとしたらIDがnullでした。プログラムバグです。")
    }

    fun findMoviesMoreThan(favoriteNum: Int): Single<List<Movie>> {
        return movieNoteDatabase.findMoreThan(favoriteNum)
                .map {
                    Timber.d("検索結果は ${it.size} ")
                    it.map { entity -> entityToMovieWithLocalInfo(entity) }
                }
    }

    private fun findMoviesFirstSearchFromMain(keyword: String): Single<List<Movie>> {
        return movieDatabase.findMovies(keyword)
                .map {
                    it.map { entity -> entityToMovieWithLocalInfo(entity) }
                }
    }

    private fun findMoviesFirstSearchFromLocalInfo(keyword: String): Single<List<Movie>> {
        return movieNoteDatabase.find(keyword)
                .map {
                    it.map { entity -> entityToMovieWithLocalInfo(entity)}
                }
    }

    private fun entityToMovieWithLocalInfo(movieNoteEntity: MovieNoteEntity): Movie {
        val entity = movieDatabase.findWithDirect(movieNoteEntity.id)
        return entity.toMovie(movieNoteEntity, categoryDatabase)
    }

    private fun entityToMovieWithLocalInfo(entity: MovieEntity): Movie {
        val localMovieInfo = movieNoteDatabase.find(entity.id)
        return entity.toMovie(localMovieInfo, categoryDatabase)
    }
}