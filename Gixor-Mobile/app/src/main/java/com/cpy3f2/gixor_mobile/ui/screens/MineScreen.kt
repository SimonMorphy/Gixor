package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.model.entity.LanguageProgress
import com.cpy3f2.gixor_mobile.model.entity.UserProfile


@Composable
fun MineScreen() {
    // 声明一个可变状态来存储数据
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }

    // 在 LaunchedEffect 中调用 fetchData 并更新状态
    LaunchedEffect(Unit) {
        userProfile = fetchData()
    }

    if (userProfile != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item { ProfileHeader(userProfile!!) }
            item { Description(userProfile!!.description) }
            item { UserStats(userProfile!!) }
            item { ScoreContainer(userProfile!!) }
            item { ProgrammingLanguages(userProfile!!.languages) }
        }
    }
}



//模拟异步 获取数据
//现在为假数据
//不需要delay(1000)
suspend fun fetchData(): UserProfile {
//    delay(1000)
    return UserProfile(
        name = "John Doe",
        jobTitle = "Android Developer",
        description = "HELLO WORLD...............................",
        followers = "1,234",
        following = "567",
        totalStars = "890",
        score = "85",
        level = "7",
        stars = "890",
        issues = "120",
        commits = "1,500",
        prs = "75",
        languages = listOf(
            LanguageProgress("Kotlin", 0f, 0.4f, Color.Blue),
            LanguageProgress("Java", 0f, 0.3f, Color.Green),
            LanguageProgress("Python", 0f, 0.2f, Color.Yellow),
            LanguageProgress("JavaScript", 0f, 0.1f, Color.Red)
        )
    )
}

@Composable
fun ProfileHeader(userProfile: UserProfile) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = userProfile.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = userProfile.jobTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Messages") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Messages")
                    },
                    onClick = {
                        // Handle message click
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings") },
                    leadingIcon = {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    },
                    onClick = {
                        // Handle settings click
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun Description(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "个人简介",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = description,
        )
    }
}

@Composable
fun UserStats(userProfile: UserProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem("Followers", userProfile.followers)
        StatItem("Following", userProfile.following)
        StatItem("Total Stars", userProfile.totalStars)
    }
}

@Composable
fun StatItem(title: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = title, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ScoreContainer(userProfile: UserProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Developer Score",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreItem("Score", userProfile.score)
                ScoreItem("Level", userProfile.level)
                ScoreItem("Stars", userProfile.stars)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreItem("Issues", userProfile.issues)
                ScoreItem("Commits", userProfile.issues)
                ScoreItem("PRs", userProfile.issues)
            }
        }
    }
}

@Composable
fun ScoreItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = title, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ProgrammingLanguages(languages: List<LanguageProgress>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Programming Languages",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            languages.forEach { language ->
                LanguageProgressBar(language)
            }
        }
    }
}

@Composable
fun LanguageProgressBar(languageProgress: LanguageProgress) {
    var currentProgress by remember { mutableFloatStateOf(languageProgress.initialProgress) }
    val animatable = remember { Animatable(initialValue = languageProgress.initialProgress) }
    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = languageProgress.targetProgress,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
        currentProgress = animatable.value
    }

    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = languageProgress.language, style = MaterialTheme.typography.bodyMedium)
            Text(text = "${(animatedProgress * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = languageProgress.color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
@Preview(showBackground = true)
@Composable
fun MineScreenPreview() {
    MaterialTheme {
        MineScreen()
    }
}