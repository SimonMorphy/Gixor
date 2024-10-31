package com.cpy3f2.gixor_mobile.ui.screens


import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.ui.components.LittleTopFunctionBar
import com.cpy3f2.gixor_mobile.viewModel.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun FrontScreen(navController: NavController,vm:MainViewModel = viewModel()) {
    HorizontalPagerWithIndicator(vm,navController)
}




//@Preview
//@Composable
//fun FrontScreenPreview(){
//    FrontScreen()
//}

@Composable
fun HorizontalPagerWithIndicator(vm: MainViewModel,navController: NavController) {
    val pages = vm.categories
    // 更新 PagerState 的创建方式
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.width(200.dp),
            ) {
                pages.forEachIndexed { index, _ ->
                    Tab(
                        text = { Text(text = pages[index].name) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
            LittleTopFunctionBar(navController)
        }

        // 更新 HorizontalPager 的使用方式
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            state = pagerState,
        ) { page ->
            when(page) {
                0 -> DynamicScreen()
                1 -> RecommendScreen()
                2 -> HotPointScreen()
            }
        }
    }
}
