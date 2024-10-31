package com.cpy3f2.gixor_mobile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController) {
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
    }

}

//@Preview
//@Composable
//fun SearchScreenPreview() {
//    SearchScreen()
//}