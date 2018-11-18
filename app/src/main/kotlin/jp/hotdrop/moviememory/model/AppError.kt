package jp.hotdrop.moviememory.model

import timber.log.Timber

// TODO 一旦。近いうちに見直す
class AppError constructor(
        private val throwable: Throwable
) {
    init {
        Timber.e(throwable)
    }
    fun getMessage() = throwable.message
    fun getStackTrace() = throwable.stackTrace
}