package com.cpy3f2.gixor_mobile.model.entity

import androidx.compose.ui.graphics.painter.Painter


/**
 * 导航栏对象
 * @property title  导航栏标题
 * @property icon 导航栏图标
 */
data class NavigationItem(
    val title :String, //导航栏标题
    val icon : Painter//导航栏图标
)
