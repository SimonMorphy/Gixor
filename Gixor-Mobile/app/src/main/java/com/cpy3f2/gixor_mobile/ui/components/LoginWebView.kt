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
import android.webkit.ConsoleMessage
import android.webkit.WebResourceError
import android.webkit.WebResourceResponse
import android.util.Log
import android.webkit.WebChromeClient
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
                                var content = document.body.textContent;
                                try {
                                    if (content.includes('You are being redirected')) {
                                        // 如果是重定向页面，替换内容
                                        document.body.innerHTML = '<div style="text-align: center; padding: 20px; font-size: 18px;">正在登录中...</div>';
                                        return;
                                    }
                                    
                                    var jsonStart = content.indexOf('{');
                                    if (jsonStart >= 0) {
                                        var jsonContent = content.substring(jsonStart);
                                        var jsonResponse = JSON.parse(jsonContent);
                                        if (jsonResponse.code === 200 && jsonResponse.token) {
                                            // 发送 token 到 Android
                                            window.androidInterface.onLoginSuccess(jsonResponse.token.tokenValue);
                                            // 替换页面内容为成功提示
                                            document.body.innerHTML = `
                                                <div style="
                                                    text-align: center; 
                                                    padding: 20px; 
                                                    font-family: system-ui, -apple-system, sans-serif;
                                                ">
                                                    <div style="
                                                        color: #1a73e8;
                                                        font-size: 24px;
                                                        margin-bottom: 10px;
                                                    ">✓</div>
                                                    <div style="
                                                        font-size: 18px;
                                                        color: #202124;
                                                    ">登录成功！</div>
                                                </div>
                                            `;
                                        }
                                    }
                                } catch(e) {
                                    console.log('Parse error:', e);
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