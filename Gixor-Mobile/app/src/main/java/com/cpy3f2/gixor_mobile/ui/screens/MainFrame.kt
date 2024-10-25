package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.model.entity.NavigationItem


@Composable
fun MainFrame() {
    var currentSelectedItem by remember { mutableIntStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("首页", painterResource(id = R.mipmap.front)),
        NavigationItem("消息", painterResource(id = R.mipmap.message)),
        NavigationItem("我的", painterResource(id = R.mipmap.github)),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(Modifier.height(60.dp)) {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(painter = item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentSelectedItem == index,
                        onClick = { currentSelectedItem = index }
                    )
                }
            }
        }
    ) {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentSelectedItem) {
                0 -> FrontScreen()
                1 -> MessageScreen()
                2 -> MineScreen()
            }
        }
}
}
@Preview
@Composable
fun MainFramePreview() {
    MainFrame()
}


