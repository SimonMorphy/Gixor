package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cpy3f2.gixor_mobile.ui.components.FocusContent
import com.cpy3f2.gixor_mobile.viewModel.MainViewModel

@Composable
fun DynamicScreen(vm: MainViewModel = viewModel()) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, bottom = 12.dp, top = 8.dp)// 增加高度
        ) {
            items(vm.focusData.size) { index ->
                FocusContent(
                    focusItem = vm.focusData[index],
                    modifier = Modifier.padding(10.dp)  // 添加水平间距
                )
            }
        }
        HorizontalDivider()
        LazyColumn {
            
        }
    }
}

@Preview
@Composable
fun DynamicScreenPreview() {
    DynamicScreen()
}
