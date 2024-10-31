package com.cpy3f2.gixor_mobile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.cpy3f2.gixor_mobile.viewModel.MainViewModel
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController,vm: MainViewModel = viewModel()) {
    var text by remember {
        mutableStateOf("")
    }

    var active by remember {
        mutableStateOf(false)
    }
    //搜索栏和返回按钮
    Scaffold {
        Spacer(modifier = Modifier.height(100.dp))
        Box{
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.width(30.dp)
            ){
                Icon(painter = painterResource(id = R.mipmap.back) , contentDescription ="返回" )
            }
            SearchBar(
                query = text,
                onQueryChange ={
                    text = it
                },
                onSearch = {
                    active = false
                },
                active = active,
                onActiveChange ={
                    active = false
                },
                placeholder = {
                    Text("请输入搜索内容")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "搜索图标"
                    )
                },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            text = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close, contentDescription = "清除图标"
                            )
                        }
                    }
                }
            ) {

            }
        }
        //写搜索历史
        SearchHistoryComponent()
    }

}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchHistoryComponent()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchHistoryComponent() {
    Column {
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
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f),  // 设置半透明的浅灰色背景
                        shape = RoundedCornerShape(4.dp)  // 添加圆角
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)  // 添加内边距使文字不贴边
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

        }
    }
}

//每一项的组件
@Composable
fun ChipItem(text: Text,onClick:()->Unit={}){
    FilterChip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        leadingIcon = {},
        border = BorderStroke(1.dp, Color(0xFF3B3A3C)),
        label = {
//            Text(text = text)
        },
        selected = false
    )
}