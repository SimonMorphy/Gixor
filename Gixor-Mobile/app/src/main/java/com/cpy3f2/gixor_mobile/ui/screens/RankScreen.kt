package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.ui.components.LittleTopFunctionBar

@Composable
fun RankScreen(navController: NavController) {
    Column {
        Row {
            LittleTopFunctionBar(navController)
        }
    }

}
//@Preview
//@Composable
//fun RankScreenPreview(){
//    RankScreen()
//}