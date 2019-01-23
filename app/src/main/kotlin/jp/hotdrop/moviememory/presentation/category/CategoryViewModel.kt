package jp.hotdrop.moviememory.presentation.category

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.usecase.CategoryUseCase
import timber.log.Timber
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
        private val useCase: CategoryUseCase
): ViewModel(), LifecycleObserver {

    private val mutableCategories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = mutableCategories

    private val mutableSuccess = MutableLiveData<OperationType>()
    val success: LiveData<OperationType> = mutableSuccess

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        refresh()
    }

    fun refresh() {
        useCase.findAll()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableCategories.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "Error! Operation add category.."))
                        }
                ).addTo(compositeDisposable)
    }

    fun add(category: Category) {
        useCase.add(category)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableSuccess.postValue(OperationType.ADD)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "Error! Operation add category.."))
                        }
                ).addTo(compositeDisposable)
    }

    fun update(category: Category) {
        useCase.update(category)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableSuccess.postValue(OperationType.UPDATE)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "Error! Operation update category name.."))
                        }
                ).addTo(compositeDisposable)
    }

    fun delete(category: Category) {
        useCase.delete(category)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableSuccess.postValue(OperationType.DELETE)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "Error! Operation update category name.."))
                        }
                ).addTo(compositeDisposable)
    }

    fun integrate(fromCategory: Category, toCategory: Category) {
            useCase.integrate(fromCategory, toCategory)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                mutableSuccess.postValue(OperationType.UPDATE)
                            },
                            onError = {
                                mutableError.postValue(AppError(it, "Error! Operation update category name.."))
                            }
                    ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        enum class OperationType { ADD, UPDATE, DELETE, REPLACE }
    }
}