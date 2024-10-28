package com.cpy3f2.gixor_mobile.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.cpy3f2.gixor_mobile.model.entity.FocusItem



@Composable
fun FocusContent(focusItem: FocusItem, modifier: Any) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)){
        FocusImage(focusItem)
        Text(text = focusItem.name, style = TextStyle(fontSize = 10.sp))
    }
}
@Preview
@Composable
fun FocusContentPreview(){
    FocusContent(
        focusItem = FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}
@Composable
fun FocusImage(focusItem: FocusItem) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(focusItem.imageUrl)
                .size(50, 50)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().clip(CircleShape),  // 填充整个 Box
            contentScale = ContentScale.Crop,
        )
    }
}
