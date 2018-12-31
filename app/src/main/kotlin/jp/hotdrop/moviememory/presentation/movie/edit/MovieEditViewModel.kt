package jp.hotdrop.moviememory.presentation.movie.edit

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieEditViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovie = MutableLiveData<Movie>()
    var movie: LiveData<Movie> = mutableMovie

    private val mutableCategories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = mutableCategories

    private val mutableSaveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = mutableSaveSuccess

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    private var category: Category? = null

    fun find(id: Long) {
        useCase.findMovie(id)
                .subscribeBy(
                        onSuccess = {
                            mutableMovie.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "映画情報の編集画面 取得"))
                        }
                ).addTo(compositeDisposable)
    }

    fun findCategories() {
        useCase.findCategories()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableCategories.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "映画情報のカテゴリー 取得"))
                        }
                ).addTo(compositeDisposable)
    }

    fun stockCategory(name: String) {
        Timber.d("$name カテゴリーをストックします。")
        category = categories.value?.find {
            it.name == name
        }
    }

    fun save(movie: Movie) {
        category?.run {
            Timber.d("${this.name} カテゴリーに変更します。")
            movie.category = this
        }
        useCase.save(movie)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableSaveSuccess.postValue(true)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "映画情報の編集画面 保存"))
                        }
                ).addTo(compositeDisposable)
    }

    fun saveMyNote(movie: Movie) {
        useCase.saveLocalEdit(movie)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableSaveSuccess.postValue(true)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "映画情報の自分用メモ編集画面 保存"))
                        }
                ).addTo(compositeDisposable)
    }

    fun clear() {
        mutableSaveSuccess.postValue(false)
        mutableError.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}