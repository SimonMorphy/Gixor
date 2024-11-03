import android.content.Context

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("gixor_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString("token", token)
            .putString("state", "login")
            .apply()
    }

    fun getToken(): String? = sharedPreferences.getString("token", null)

    fun hasToken(): Boolean = getToken() != null

    fun clearToken() {
        sharedPreferences.edit()
            .remove("token")
            .remove("state")
            .remove("userInfo")
            .apply()
    }

    fun saveUserInfo(jsonResponse: String) {
        sharedPreferences.edit()
            .putString("userInfo", jsonResponse)
            .apply()
    }

    fun getUserInfoJson(): String? = sharedPreferences.getString("userInfo", null)
} 