package com.cpy3f2.gixor_mobile.model.entity
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

/**
 * GitHub Event
 * @author lya
 * @since 2024/11/5
 */
data class Event(
    val id: String,
    val type: String,
    val actor: Actor,
    val repo: Repo,
    val payload: Payload,
    val isPublic: Boolean,

    @SerializedName("created_at")
    val createdAt: String
) {

    data class Actor(
        val id: Long,
        val login: String,
        @SerializedName("display_login")
        val displayLogin: String,
        @SerializedName("gravatar_id")
        val gravatarId: String,
        val url: String,
        @SerializedName("avatar_url")
        val avatarUrl: String
    )

    data class Repo(
        val id: Long,
        val name: String,
        val url: String
    )

    data class Payload(
        val action: String,
        @SerializedName("push_id")
        val pushId: Long,
        val size: Int,
        @SerializedName("distinct_size")
        val distinctSize: Int,
        val ref: String,
        val head: String,
        val before: String,
        val description: String,
        val masterBranch: String
    )
}
