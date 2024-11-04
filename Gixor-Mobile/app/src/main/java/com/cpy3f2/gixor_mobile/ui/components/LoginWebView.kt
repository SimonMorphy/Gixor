import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.app.Activity
import android.graphics.Bitmap

import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.onSizeChanged

import kotlinx.coroutines.delay

@Composable
fun LoginWebView(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webView: WebView? = null
    var loadingProgress by remember { mutableStateOf(0f) }
    var isLoading by remember { mutableStateOf(true) }

    // 收集登录状态
    val loginState by viewModel.loginState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        // 进度条
        if (isLoading && loadingProgress < 1f) {
            LinearProgressIndicator(
                progress = loadingProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webView = this
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }

                    // 设置WebView的背景为透明
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)

                    // 添加进度监听
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            loadingProgress = newProgress / 100f
                            isLoading = newProgress < 100
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false

                            // 注入 JavaScript 来处理响应
                            val script = """
                                (function() {
                                    const messageStyle = 
                                        'position: fixed;' +
                                        'top: 50%;' +
                                        'left: 50%;' +
                                        'transform: translate(-50%, -50%);' +
                                        'text-align: center;' +
                                        'padding: 20px;' +
                                        'font-family: system-ui, -apple-system, sans-serif;' +
                                        'width: 100%;';

                                    var content = document.body.textContent || '';
                                    var url = window.location.href;
                                    
                                    // 仅在重定向页面显示登录中
                                    if (content.includes('You are being redirected')) {
                                        document.body.innerHTML = 
                                            '<div style="' + messageStyle + '">正在登录中...</div>';
                                        return;
                                    }
                                    
                                    // 只在登录回调页面处理登录结果
                                    if (url.includes('/auth/callback')) {
                                        var jsonStart = content.indexOf('{');
                                        if (jsonStart >= 0) {
                                            try {
                                                var jsonContent = content.substring(jsonStart);
                                                var jsonResponse = JSON.parse(jsonContent);
                                                
                                                if (jsonResponse.code === 200 && jsonResponse.token) {
                                                    window.androidInterface.onLoginSuccess(jsonResponse.token.tokenValue);
                                                    document.body.innerHTML = '';
                                                } else {
                                                    document.body.innerHTML = 
                                                        '<div style="' + messageStyle + '">' +
                                                            '<div style="color: #d93025; font-size: 24px; margin-bottom: 10px;">✕</div>' +
                                                            '<div style="font-size: 18px; color: #202124;">登录失败</div>' +
                                                            '<div style="color: #5f6368; margin-top: 8px;">' + 
                                                                jsonResponse.msg + 
                                                            '</div>' +
                                                        '</div>';
                                                }
                                            } catch(e) {
                                                console.error('Failed to parse JSON:', e);
                                            }
                                        }
                                    }
                                })();
                            """.trimIndent()

                            view?.evaluateJavascript(script, null)
                        }
                    }

                    // 添加 JavaScript 接口
                    addJavascriptInterface(
                        object {
                            @JavascriptInterface
                            fun onLoginSuccess(token: String) {
                                (context as? Activity)?.runOnUiThread {
                                    viewModel.handleLoginSuccess(token)
                                }
                            }
                        },
                        "androidInterface"
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { webView ->
            webView.loadUrl("http://1024.viwipiediema.com:10102/auth/render")
        }
    }

    // 处理登录状态
    LaunchedEffect(loginState) {
        when (loginState) {
            is MainViewModel.LoginState.Success -> {
                delay(500) // 短暂延迟以确保数据保存完成
                onLoginSuccess() // 调用导航回调
            }
            is MainViewModel.LoginState.Error -> {
                // 处理错误情况
            }
            else -> {}
        }
    }

    // 清理 WebView
    DisposableEffect(Unit) {
        onDispose {
            webView?.destroy()
        }
    }
}
