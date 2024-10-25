package com.cpy3f2.gixor_mobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.rememberAsyncImagePainter
import com.cpy3f2.gixor_mobile.model.entity.FocusItem



@Composable
fun FocusContent(focusItem: FocusItem) {
    Column {
        Image(
            painter = rememberAsyncImagePainter(model = focusItem.imageUrl),
            contentDescription = null,
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)
        Text(text = focusItem.name)
    }
}
@Preview
@Composable
fun FocusContentPreview(){
    FocusContent(focusItem = FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据1"))
}