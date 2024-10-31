package com.cpy3f2.gixor_mobile.model.entity
import androidx.compose.ui.graphics.Color

data class UserProfile(
    val name: String,
    val jobTitle: String,
    val description: String,
    val followers: String,
    val following: String,
    val totalStars: String,
    val score: String,
    val level: String,
    val stars: String,
    val issues: String,
    val commits: String,
    val prs: String,
    val languages: List<LanguageProgress>
)
data class LanguageProgress(
    val language: String,
    val initialProgress: Float,
    val targetProgress: Float,
    val color: Color
)