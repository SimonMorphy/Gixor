import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.app.Activity

import android.webkit.JavascriptInterface
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import kotlinx.coroutines.delay

@Composable
fun LoginWebView(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webView: WebView? = null
    
    // 收集登录状态
    val loginState by viewModel.loginState.collectAsState()
    
    // 处理登录状态
    LaunchedEffect(loginState) {
        when (loginState) {
            is MainViewModel.LoginState.Success -> {
                delay(1500)
                onLoginSuccess()
            }
            is MainViewModel.LoginState.Error -> {
                // 处理错误
            }
            else -> {}
        }
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
                
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        
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
                                                document.body.innerHTML = 
                                                    '<div style="' + messageStyle + '">' +
                                                        '<div style="color: #1a73e8; font-size: 24px; margin-bottom: 10px;">⌛</div>' +
                                                        '<div style="font-size: 18px; color: #202124;">正在获取用户信息...</div>' +
                                                    '</div>';
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
        modifier = modifier.fillMaxSize()
    ) { webView ->
        webView.loadUrl("http://1024.viwipiediema.com:10102/auth/render")
    }

    // 清理 WebView
    DisposableEffect(Unit) {
        onDispose {
            webView?.destroy()
        }
    }
} 