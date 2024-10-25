package com.cpy3f2.gixor_mobile.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cpy3f2.gixor_mobile.model.entity.Category
import com.cpy3f2.gixor_mobile.model.entity.FocusItem

class MainViewModel: ViewModel() {
    var categories by mutableStateOf(
        listOf( Category("推荐"),
                Category("动态"),
                Category("关注")
        )
    )

    var categoryIndex by mutableIntStateOf(0)
        private set

    /**
     * 更新分类下表
     * @param index
     */
    fun updateCategoriesIndex(index: Int){
        categoryIndex = index
    }

    /**
     *获取关注数据
     */
    //TODO 获取网络请求中的数据
    var focusData  =  listOf(FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据1"))
}