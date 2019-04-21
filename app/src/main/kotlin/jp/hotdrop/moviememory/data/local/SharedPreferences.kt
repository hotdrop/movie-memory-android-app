package jp.hotdrop.moviememory.data.local

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

class SharedPreferences @Inject constructor(
        context: Context
) {

    private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    var dateOfLastGetMovieFromRemote: Long
        get() = sharedPrefs.getLong(KEY_LAST_DATE_FROM_REMOTE, 0)
        set(value) {
            sharedPrefs.edit().run {
                putLong(KEY_LAST_DATE_FROM_REMOTE, value)
                apply()
            }
        }

    companion object {
        private const val KEY_LAST_DATE_FROM_REMOTE = "k1aif0li83sue946p1kis"
    }
}