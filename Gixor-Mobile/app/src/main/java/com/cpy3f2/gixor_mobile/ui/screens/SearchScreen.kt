package com.cpy3f2.gixor_mobile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem
import com.cpy3f2.gixor_mobile.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, vm: MainViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // 记住搜索历史列表
    var searchHistory by remember { mutableStateOf(listOf<SearchHistoryItem>()) }

    Scaffold(
        topBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 顶部搜索栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.mipmap.back),
                    contentDescription = "返回",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                SearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { 
                        active = false
                        // 添加搜索历史
                        if (searchText.isNotEmpty()) {
                            val newItem = SearchHistoryItem(
                                id = 0, // Room会自动生成ID
                                name = searchText,
                                time = LocalDateTime.now()
                            )
                            // 这里需要调用ViewModel的方法来保存搜索历史
                            // vm.addSearchHistory(newItem)
                        }
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("搜索游戏、帖子、用户") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = SearchBarDefaults.colors(
                        containerColor = Color(0xFFF5F5F5),
                        dividerColor = Color.Transparent
                    )
                ) {
                    // 搜索建议内容
                    if (searchText.isNotEmpty()) {
                        LazyColumn {
                            items(5) { index ->
                                ListItem(
                                    headlineContent = { Text("搜索建议 $searchText $index") },
                                    leadingContent = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    },
                                    modifier = Modifier.clickable { }
                                )
                            }
                        }
                    }
                }
            }
            SearchHistoryComponent(vm);

            // 搜索历史
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (searchHistory.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "搜索历史",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            painter = painterResource(id = R.mipmap.clear),
                            contentDescription = "清除",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { 
                                    // vm.clearSearchHistory()
                                }
                        )
                    }
                    
                    LazyColumn(
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        items(searchHistory.sortedByDescending { it.time }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        searchText = item.name
                                        active = false
                                    }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.DarkGray
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "删除",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { 
                                            // vm.deleteSearchHistory(item)
                                        },
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
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