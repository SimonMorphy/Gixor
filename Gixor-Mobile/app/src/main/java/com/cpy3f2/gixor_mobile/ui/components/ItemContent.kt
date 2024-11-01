package com.cpy3f2.gixor_mobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository

@Composable
fun ItemContent(gitHubRepository: GitHubRepository) {
    Card (
        modifier = Modifier.fillMaxWidth()
    ){
        Column {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(gitHubRepository.owner?.avatarUrl)
                    .size(80,80)
                    .build()
            )
            //TODO 根据某个字段判断是否收藏了某个项目
            val icon  =  painterResource(id = R.mipmap.star)
            Row {
                Image(painter =painter , contentDescription = "作者头像")
                Column {
                    Text(text = gitHubRepository.fullName?:"")
                }
                IconButton(
                    onClick = {
                         // TODO:添加收藏功能
                    },
                ) {
                    Icon(painter =icon, contentDescription ="stat")
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemContentPreview() {
    ItemContent(GitHubRepository())
}
