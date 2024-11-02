import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove(KEY_TOKEN)
            .apply()
    }

    fun hasToken(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    companion object {
        private const val PREF_NAME = "GixorPrefs"
        private const val KEY_TOKEN = "user_token"
    }
} 