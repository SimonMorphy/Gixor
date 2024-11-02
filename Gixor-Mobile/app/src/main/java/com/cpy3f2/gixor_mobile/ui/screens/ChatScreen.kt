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
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.rounded.KeyboardArrowDown


@Composable
fun ChatScreen(
    viewModel: MainViewModel,
    userId: String,
    userName: String
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.background
    val isDarkIcons = !isSystemInDarkTheme()
    
    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = isDarkIcons
        )
        onDispose {}
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = { BottomInput() }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {  // 使用Box来放置聊天内容和滚动按钮
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 顶部栏
                TopBar(viewModel, userName)
                
                // 聊天内容
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val listState = rememberLazyListState()
                    val messageCount = 20 // 假设有20条消息
                    
                    // 首次加载时滚动到底部
                    LaunchedEffect(Unit) {
                        // 滚动到最后一条消息
                        listState.scrollToItem(
                            index = messageCount - 1,
                            scrollOffset = 0
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5)),
                        state = listState,
                        reverseLayout = false  // 改为false，使消息正序显示
                    ) {
                        items(messageCount) { index ->
                            if (index % 2 == 0) {
                                ReceivedMessage("这是收到的消息 $index")
                            } else {
                                SentMessage("这是发送的消息 $index")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(viewModel: MainViewModel, userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 返回按钮
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "返回",
            modifier = Modifier
                .size(24.dp)
                .clickable { viewModel.navController.value?.popBackStack() }
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // 用户名
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ChatContent() {
    val listState = rememberLazyListState()
    val messageCount = 20 // 假设有20条消息
    
    // 首次加载时滚动到底部
    LaunchedEffect(Unit) {
        // 滚动到最后一条消息
        listState.scrollToItem(
            index = messageCount - 1,
            scrollOffset = 0
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        state = listState,
        reverseLayout = false  // 改为false，使消息正序显示
    ) {
        items(messageCount) { index ->
            if (index % 2 == 0) {
                ReceivedMessage("这是收到的消息 $index")
            } else {
                SentMessage("这是发送的消息 $index")
            }
        }
    }
}

@Composable
private fun ReceivedMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // 头像
        AsyncImage(
            model = "https://placekitten.com/200/200",
            contentDescription = "头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        // 消息气泡
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                .background(Color.White)
                .padding(12.dp)
        ) {
            Text(text = message)
        }
    }
}

@Composable
private fun SentMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // 消息气泡
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        
        // 头像
        AsyncImage(
            model = "https://placekitten.com/200/200",
            contentDescription = "头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomInput() {
    var text by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 输入框
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("发送消息...") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (text.isNotEmpty()) {
                            text = ""
                        }
                    }
                )
            )
            
            // 发送按钮
            Button(
                onClick = { 
                    if (text.isNotEmpty()) {
                        text = ""
                    }
                },
                shape = CircleShape,
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "发送",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// 可选：添加一个滚动到底部的按钮
@Composable
private fun ScrollToBottomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowDown,
            contentDescription = "滚动到底部",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
} 