package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.MovieNoteDatabase
import jp.hotdrop.moviememory.data.local.SuggestionDatabase
import jp.hotdrop.moviememory.data.local.entity.*
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class SearchDataRepository @Inject constructor(
        private val movieDatabase: MovieDatabase,
        private val movieNoteDatabase: MovieNoteDatabase,
        private val suggestionDatabase: SuggestionDatabase
): SearchRepository {

    override fun findCategories(): Single<List<Category>> {
        return movieDatabase.findCategories()
                .map { entities ->
                    Timber.d("取得したカテゴリー数 ${entities.size}")
                    entities.map {
                        Timber.d( "  id=${it.id} name=${it.name}")
                        it.toCategory()
                    }
                }
    }

    override fun suggestion(): Flowable<List<Suggestion>> =
            suggestionDatabase.suggestion()
                    .map {
                        it.map { entity -> entity.toSuggestion() }
                    }

    // TODO 同じキーワードはIDを振り直して先頭に持っていきたいが、autogenerateしてると無理。考える
    override fun save(suggestion: Suggestion): Completable =
            Completable.create { emitter ->
                suggestionDatabase.save(suggestion.toEntity())
                emitter.onComplete()
            }

    override fun deleteSuggestion(): Completable =
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
    override fun findMovies(suggestion: Suggestion): Single<List<Movie>> {
        return Single.zip<List<Movie>, List<Movie>, List<Movie>>(
                findMoviesFirstSearchFromMain(suggestion),
                findMoviesFirstSearchFromLocalInfo(suggestion),
                BiFunction { t1, t2 ->
                    t1.plus(t2).distinctBy { it.id }
                }
        )
    }

    private fun findMoviesFirstSearchFromMain(suggestion: Suggestion): Single<List<Movie>> {
        return movieDatabase.findMovies(suggestion)
                .map {
                    it.map { entity -> entityToMovieWithLocalInfo(entity) }
                }
    }

    private fun findMoviesFirstSearchFromLocalInfo(suggestion: Suggestion): Single<List<Movie>> {
        return movieNoteDatabase.find(suggestion)
                .map {
                    it.map { entity -> entityToMovieWithLocalInfo(entity)}
                }
    }

    private fun entityToMovieWithLocalInfo(movieNoteEntity: MovieNoteEntity): Movie {
        val entity = movieDatabase.findWithDirect(movieNoteEntity.id)
        return entity.toMovie(movieNoteEntity)
    }

    private fun entityToMovieWithLocalInfo(entity: MovieEntity): Movie {
        val localMovieInfo = movieNoteDatabase.find(entity.id)
        return entity.toMovie(localMovieInfo)
    }
}