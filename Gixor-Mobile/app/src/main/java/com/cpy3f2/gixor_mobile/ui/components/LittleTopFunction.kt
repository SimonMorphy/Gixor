package com.cpy3f2.gixor_mobile.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.R

@Composable
fun LittleTopFunctionBar(navController: NavController) {
    Row{
        Spacer(modifier = Modifier.weight(1f))
        // 搜索
        IconButton(
            onClick = {
                navController.navigate("search")
            },
            Modifier.align(Alignment.CenterVertically).width(25.dp)) {
            Icon(
                painter = painterResource(id = R.mipmap.search),
                contentDescription = "搜索"
            )
        }
        // 消息
        IconButton(
            onClick = { /*TODO*/ },
            Modifier.align(Alignment.CenterVertically).padding(end = 5.dp)) {
            Icon(
                painter = painterResource(id = R.mipmap.message),
                contentDescription = "消息"
            )
        }
    }
}

