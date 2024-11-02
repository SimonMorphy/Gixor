import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.ui.tooling.preview.Preview
import com.cpy3f2.gixor_mobile.navigation.NavigationManager


@Composable
fun ChatScreen(
    viewModel: MainViewModel,
    notificationId: String,
    repository: String
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = { 
            IssueTopBar(
                viewModel = viewModel,
                repository = repository
            ) 
        },
        bottomBar = { IssueCommentInput() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Issue Header
            item {
                IssueHeader(
                    title = "Unable to load specific user profile",
                    number = notificationId,  // 使用通知ID作为issue编号
                    status = IssueStatus.OPEN,
                    author = "Author Name",  // 这里可以从viewModel获取作者信息
                    createdTime = "2024-03-20 10:30"  // 这里可以从viewModel获取创建时间
                )
            }

            // Issue Description
            item {
                IssueComment(
                    author = "Author Name",
                    avatar = "https://placekitten.com/200/200",
                    content = "这里是问题的详细描述...",
                    time = "3天前",
                    isAuthor = true
                )
            }

            // Comments
            items(10) { index ->
                IssueComment(
                    author = "User ${index + 1}",
                    avatar = "https://placekitten.com/200/200",
                    content = "这是第 ${index + 1} 条回复内容...",
                    time = "${index + 1}天前",
                    isAuthor = false
                )
            }
        }
    }
}

enum class IssueStatus { OPEN, CLOSED }

@Composable
private fun IssueTopBar(
    viewModel: MainViewModel,
    repository: String
) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "返回",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { 
                        NavigationManager.navigateBack()
                    }
            )
            
            Text(
                text = "#123",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun IssueHeader(
    title: String,
    number: String,
    status: IssueStatus,
    author: String,
    createdTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = when(status) {
                    IssueStatus.OPEN -> Color(0xFF2DA44E)
                    IssueStatus.CLOSED -> Color(0xFF8250DF)
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when(status) {
                            IssueStatus.OPEN -> Icons.Rounded.Info
                            IssueStatus.CLOSED -> Icons.Rounded.Check
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = status.name,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            
            Text(
                text = "$author opened this issue on $createdTime",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IssueComment(
    author: String,
    avatar: String,
    content: String,
    time: String,
    isAuthor: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Comment header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Text(
                text = author,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
            
            if (isAuthor) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Author",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Comment content
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IssueCommentInput() {
    var text by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        shadowElevation = 8.dp
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Leave a comment") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            maxLines = 4
        )
    }
}

@Preview
@Composable
fun IssueCommentPreview() {
    IssueComment(
        author = "John Doe",
        avatar = "https://placekitten.com/200/200",
        content = "This is a sample comment with some content that might span multiple lines. " +
                "It's designed to show how the comment layout works with longer text.",
        time = "3天前",
        isAuthor = true
    )
} 