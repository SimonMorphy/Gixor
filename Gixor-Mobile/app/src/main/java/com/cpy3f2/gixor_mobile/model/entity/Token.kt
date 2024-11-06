package com.cpy3f2.gixor_mobile.model.entity

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
@Entity("token")
data class Token(
    @SerializedName("tokenName")  // 或 @JsonProperty("tokenName")
    val tokenName: String,
    @SerializedName("tokenValue")
    val tokenValue: String,
    @SerializedName("isLogin")
    val isLogin: Boolean,
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("loginType")
    val loginType: String,
    @SerializedName("tokenTimeout")
    val tokenTimeout: Int,
    @SerializedName("sessionTimeout")
    val sessionTimeout: Int,
    @SerializedName("tokenSessionTimeout")
    val tokenSessionTimeout: Int,
    @SerializedName("tokenActiveTimeout")
    val tokenActiveTimeout: Int,
    @SerializedName("loginDevice")
    val loginDevice: String,
    @SerializedName("tag")
    val tag: String? = null
)