package com.cpy3f2.gixor_mobile.model.entity
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UserProfile(
    val data: GitHubUser?,
    val languageProgress: List<LanguageProgress>
)

data class LanguageProgress(
    val language: String,
    val currentProgress: Float,
    val targetProgress: Float,
    val color: Color
)

// 假设 gitHubUser 是一个 StateFlow 或 MutableStateFlow
