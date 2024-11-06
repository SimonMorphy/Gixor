package com.cpy3f2.gixor_mobile.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.model.entity.NavigationItem

@Composable
fun MainFrame(navController: NavController) {
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("退出应用") },
            text = { Text("确定要退出应用吗？") },
            confirmButton = {
                TextButton(onClick = {
                    // 退出应用
                    android.os.Process.killProcess(android.os.Process.myPid())
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    var currentSelectedItem by remember { mutableIntStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("首页", painterResource(id = R.mipmap.front)),
        NavigationItem("排行", painterResource(id = R.mipmap.rank)),
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
                0 -> FrontScreen(navController)
                1 -> RankScreen()
                2 -> MineScreen()
            }
        }
}
}
@Preview
@Composable
fun MainFramePreview() {
//    MainFrame()
}


