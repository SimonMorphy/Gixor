package com.cpy3f2.gixor_mobile.ui.screens

import RankViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.ui.components.LittleTopFunctionBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RankScreen(
    navController: NavController,
    viewModel: RankViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        Row {
            LittleTopFunctionBar(navController)
        }
        
        // 筛选条件区域
        Column(modifier = Modifier.padding(16.dp)) {
            // 领域筛选
            FilterSection(
                title = "领域",
                options = listOf("Android", "AI", "Web", "iOS"),
                selectedOption = uiState.selectedDomain,
                onOptionSelected = { viewModel.updateDomain(it) }
            )
            
            // 国家筛选
            FilterSection(
                title = "国家",
                options = listOf("中国", "美国", "日本", "韩国"),
                selectedOption = uiState.selectedCountry,
                onOptionSelected = { viewModel.updateCountry(it) }
            )
            
            // 编程语言筛选
            FilterSection(
                title = "编程语言",
                options = listOf("Java", "Python", "C++", "JavaScript"),
                selectedOption = uiState.selectedLanguage,
                onOptionSelected = { viewModel.updateLanguage(it) }
            )
        }
        
        // 这里添加排行榜列表显示
    }
}

@Composable
private fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title)
        LazyRow {
            items(options.size) { index ->
                FilterChip(
                    selected = selectedOption == options[index],
                    onClick = { onOptionSelected(options[index]) },
                    label = { Text(options[index]) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
//@Preview
//@Composable
//fun RankScreenPreview(){
//    RankScreen()
//}