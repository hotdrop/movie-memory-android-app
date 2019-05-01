package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class CategoryAttachViewModel @Inject constructor(
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

    fun find(id: Long) {
        useCase.findMovie(id)
                .subscribeBy(
                        onSuccess = {
                            mutableMovie.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "カテゴリー設定 映画情報取得"))
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
                            mutableError.postValue(AppError(it, "カテゴリー設定 カテゴリー一覧取得"))
                        }
                ).addTo(compositeDisposable)
    }

    fun save(categoryName: String) {
        Timber.d("カテゴリーを $categoryName に設定します。")
        categories.value
                ?.find { it.name == categoryName }
                ?.run {
                    val newMovie = movie.value!!.copy(category = this)
                    useCase.save(newMovie)
                            .observeOn(Schedulers.io())
                            .subscribeBy(
                                    onComplete = {
                                        mutableSaveSuccess.postValue(true)
                                    },
                                    onError = {
                                        mutableError.postValue(AppError(it, "カテゴリー設定 保存"))
                                    }
                            ).addTo(compositeDisposable)
                }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}