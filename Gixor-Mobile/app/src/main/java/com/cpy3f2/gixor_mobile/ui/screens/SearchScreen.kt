package com.cpy3f2.gixor_mobile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import kotlinx.coroutines.launch

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.material3.IconButton

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, vm: MainViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("人物热榜", "项目热榜")
    val searchHistory by vm.searchHistoryItems.collectAsState()
    
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    fun handleSearch() {
        if (searchText.isNotBlank()) {
            vm.addSearchHistory(searchText)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // 搜索栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.mipmap.back),
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )

                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardActions = KeyboardActions(onSearch = { handleSearch() }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        decorationBox = { innerTextField ->
                            Box {
                                if (searchText.isEmpty()) {
                                    Text(
                                        "搜索项目、用户、方向",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    if (searchText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "清除",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { searchText = "" }
                        )
                    }
                }
            }
        }

        // 搜索历史
        SearchHistoryComponent(
            vm = vm,
            onHistoryItemClick = { historyText ->
                searchText = historyText
                handleSearch()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 热榜标签栏
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = { },
            indicator = { }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = if (selectedTab == index) 18.sp else 16.sp,
                            color = if (selectedTab == index) Color.Red else Color.Black,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // 热榜内容
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> PersonHotList()
                1 -> ProjectHotList()
            }
        }
    }
}

@Composable
private fun PersonHotList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(15) { index ->
            key(index) {  // 使用 key 优化重组
                HotListItem(
                    rank = index + 1,
                    title = "热门用户 ${index + 1}",
                    subtitle = "活跃度: ${1000 - index * 50}",
                    score = "${1000 - index * 50}"
                )
            }
        }
    }
}

@Composable
private fun ProjectHotList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(15) { index ->
            key(index) {
                HotListItem(
                    rank = index + 1,
                    title = "热门项目 ${index + 1}",
                    subtitle = "热度: ${1000 - index * 50}",
                    score = "${1000 - index * 50}"
                )
            }
        }
    }
}

@Composable
private fun HotListItem(
    rank: Int,
    title: String,
    subtitle: String,
    score: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排名
        Text(
            text = rank.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = when (rank) {
                1 -> Color(0xFFFF6B6B)
                2 -> Color(0xFF6C757D)
                3 -> Color(0xFFCD7F32)
                else -> Color.Gray
            },
            modifier = Modifier.width(40.dp)
        )

        // 内容
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // 分数
        Text(
            text = score,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchHistoryComponent(
    vm: MainViewModel,
    onHistoryItemClick: (String) -> Unit
) {
    val searchHistory by vm.searchHistoryItems.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        if (searchHistory.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "搜索历史",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // 修改清除按钮部分
                Row(
                    modifier = Modifier
                        .clickable { vm.clearSearchHistory() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.clear),
                        contentDescription = "清除历史",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "清除",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isExpanded) 
                            Modifier.wrapContentHeight()
                        else 
                            Modifier.height(72.dp)
                    )
                    .clip(RectangleShape)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    searchHistory.forEach { item ->
                        ChipItem(
                            text = item.name,
                            onClick = { onHistoryItemClick(item.name) }
                        )
                    }
                }
            }
            
            // 如果有超过两行的内容，显示展开/收起按钮
            if (searchHistory.size > 6) { // 假设每行平均显示3个项目
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) "收起" else "展开",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = if (isExpanded) 
                                Icons.Default.KeyboardArrowUp 
                            else 
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "收起" else "展开",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

//每一项的组件
@Composable
fun ChipItem(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun HotPoint(vm: MainViewModel){
    val pages = vm.hotList
    val pagerState = rememberPagerState{pages.size}
    val scope = rememberCoroutineScope()

    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.width((100.dp * pages.size) + (10.dp * (pages.size - 1))),
                divider = {},
                indicator = {}
            ) {
                pages.forEachIndexed { index, _ ->
                    Tab(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(0.dp),
                        text = {
                            Text(
                                text = pages[index],
                                fontSize = 14.sp,
                                color = if (pagerState.currentPage == index) Color.Red else Color.Black
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        interactionSource = remember { MutableInteractionSource() },
                    )
                }
            }
        }
        // 更新 HorizontalPager 的使用方式
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            state = pagerState,
        ) { page ->
            when(page) {
                0 -> PersonHotPoint()
                1 -> ProjectHotPoint()
            }
        }
    }
}

@Composable
fun PersonHotPoint(){

}

@Composable
fun ProjectHotPoint(){

}

