package jp.hotdrop.moviememory.model

import timber.log.Timber

class AppError constructor(
        private val throwable: Throwable? = null,
        private val message: String? = null
) {
    init {
        if (message.isNullOrEmpty()) {
            throwable?.let {
                Timber.e(it)
            } ?: throw Throwable("AppErrorを生成したにも関わらずthrowableもmessageもnullです。プログラムを見直してください")
        } else {
            throwable?.let {
                Timber.e(it, message)
            } ?: Timber.e(message)
        }
    }

    fun getMessage(): String? = message ?: throwable?.message
}