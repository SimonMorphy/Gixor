package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.model.entity.FocusItem
import com.cpy3f2.gixor_mobile.ui.components.FocusContent
import com.cpy3f2.gixor_mobile.viewModel.MainViewModel

@Composable
fun DynamicScreen(vm : MainViewModel = MainViewModel()) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
       Row (
           Modifier
               .fillMaxWidth()
               .height(50.dp)
       ){
        FocusContent(focusItem = FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据1"))
       }
    }

}
@Preview
@Composable
fun DynamicScreenPreview() {
    DynamicScreen()
}