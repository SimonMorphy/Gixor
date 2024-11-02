package com.cpy3f2.gixor_mobile.ui.screens


import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.ui.components.LittleTopFunctionBar
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
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
                indicator = {},
                divider = {},
                containerColor = Color.Transparent
            ) {
                pages.forEachIndexed { index, _ ->
                    Tab(
                        text = {
                            Text(text = pages[index].name,
                                fontSize = if (pagerState.currentPage == index) 17.sp else 14.sp,
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
