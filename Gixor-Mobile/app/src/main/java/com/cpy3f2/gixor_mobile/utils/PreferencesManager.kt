package com.cpy3f2.gixor_mobile.utils
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("gixor_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()


    fun saveToken(jsonResponse: String) {
        try {
            // 解析 JSON 响应
            val jsonObject = gson.fromJson(jsonResponse, JsonObject::class.java)
            val tokenObject = jsonObject.getAsJsonObject("token")
            val tokenValue = tokenObject.get("tokenValue").asString

            // 保存 tokenValue
            sharedPreferences.edit()
                .putString("token", tokenValue)
                .putString("state", "login")
                .apply()

        } catch (e: Exception) {
            e.printStackTrace()
        }
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