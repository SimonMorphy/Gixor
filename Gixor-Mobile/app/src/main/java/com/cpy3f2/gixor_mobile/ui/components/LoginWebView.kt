import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@Composable
fun LoginWebView(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit
) {
    var webView: WebView? = null
    
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
                                                        '<div style="color: #1a73e8; font-size: 24px; margin-bottom: 10px;">✓</div>' +
                                                        '<div style="font-size: 18px; color: #202124;">登录成功！</div>' +
                                                    '</div>';
                                            } else {
                                                document.body.innerHTML = 
                                                    '<div style="' + messageStyle + '">' +
                                                        '<div style="color: #d93025; font-size: 24px; margin-bottom: 10px;">✕</div>' +
                                                        '<div style="font-size: 18px; color: #202124;">登录失败</div>' +
                                                    '</div>';
                                            }
                                        } catch(e) {
                                            console.log('Parse error:', e);
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
                                // 延迟关闭登录页面
                                Handler(Looper.getMainLooper()).postDelayed({
                                    onLoginSuccess()
                                }, 1500) // 1.5秒后关闭
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

    // 清理 WebView
    DisposableEffect(Unit) {
        onDispose {
            webView?.destroy()
        }
    }
} 