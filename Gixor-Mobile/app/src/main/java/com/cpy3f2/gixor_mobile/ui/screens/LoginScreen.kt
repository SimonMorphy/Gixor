package com.cpy3f2.gixor_mobile.ui.screens

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun LoginScreen(navController: NavController,vm : MainViewModel = viewModel(),sharedPreferences: SharedPreferences) {
    //创建路由
    //账号
    var username by remember {
        mutableStateOf("")
    }
    //密码
    var password by remember {
        mutableStateOf("")
    }
    //密码是否符合可用
    var iSPasswordVisible by remember {
        mutableStateOf(false)
    }
    //格式是否正确
    val isFormValid by derivedStateOf {
        username.isNotBlank() && password.length>=0
    }
    Scaffold {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                painter = painterResource(id = R.mipmap.github1),
                contentDescription = "App Logo",
                modifier = Modifier
                    .weight(1f)
                    .size(200.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Card(
                Modifier
                    .weight(2f)
                    .padding(8.dp),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ){
                    Text(text = "欢迎回来!", fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("账号") },
                            singleLine = true,
                            trailingIcon = {
                                if (username.isNotBlank())
                                    IconButton(onClick = { username = "" }) {
                                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                                    }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("密码") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            visualTransformation = if (iSPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = {iSPasswordVisible=!iSPasswordVisible}) {
                                    Icon(imageVector = if (iSPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff ,
                                        contentDescription = "Password Toggle")
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            vm.login(username, password)
                            // 获取登录结果
                            val result = vm.loginData.value
                            if(result?.code==200){
                                //将 登录成功后的数据保存到数据库
                                val token = result.data
                                sharedPreferences.edit().putString("tokenValue",token.tokenValue).apply()
                                //去请求获取当前用户信息的接口
                                val resultUser = vm.gitHubUser.value
                                if(resultUser?.code==200){
                                    //将 当前用户信息保存到数据库
                                    val user = resultUser.data

                                    navController.navigate("main")
                                }

                            }

                        },
                        enabled = isFormValid,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "登录")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
//                        TextButton(onClick = { }) {
//                            Text(text = "Sign Up")
//                        }
//                        TextButton(onClick = { }) {
//                            Text(text = "Forgot Password?", color = Color.Gray)
//                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
}
