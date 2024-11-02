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
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.runtime.key

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, vm: MainViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("人物热榜", "项目热榜")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }
    
    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // 顶部搜索栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            
            // 取消按钮
            Text(
                text = "取消",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        // 修改后的标签栏
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = { }, // 移除底部分割线
            indicator = { } // 移除指示器
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
                            fontSize = if (selectedTab == index) 18.sp else 16.sp,  // 选中时字体变大
                            color = if (selectedTab == index) Color.Red else Color.Black,  // 选中时变红
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // 优化后的 HorizontalPager
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
fun SearchHistoryComponent(vm: MainViewModel) {
    Column(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "搜索历史",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.clickable { /* 在这里处理清空操作 */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.mipmap.clear),
                    contentDescription = "清空",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "清空",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        FlowRow {
            vm.searchHistoryItems.forEach { item ->
                ChipItem(text = item.name) {

                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        HotPoint(vm)
    }
}

//每一项的组件
@Composable
fun ChipItem(text: String, onClick: () -> Unit = {}) {
    FilterChip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        leadingIcon = {},
        border = BorderStroke(1.dp, Color(0xFF3B3A3C)),
        label = {
            Text(text = text)
        },
        selected = false
    )
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

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(viewModel())
}