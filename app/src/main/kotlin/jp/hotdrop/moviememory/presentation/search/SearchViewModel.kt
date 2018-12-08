package jp.hotdrop.moviememory.presentation.search

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.usecase.SearchUseCase
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val useCase: SearchUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableCategories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = mutableCategories

    fun findCategories() {
        Timber.d("カテゴリーを検索しにいきます。")
        useCase.findCategories()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableCategories.postValue(it)
                        }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}