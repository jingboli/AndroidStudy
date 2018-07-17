package cc.lijingbo.androidstudy.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import cc.lijingbo.androidstudy.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webview.settings.javaScriptEnabled = true;
        webview.webViewClient = MyWebViewClient()

        webview.loadUrl("http://sydj.cnpc.com.cn/mobile/")
    }

    fun loadUrl(view: View?) {

    }

    class MyWebViewClient : WebViewClient() {
        val TAG = "WebViewTestActivity";

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.e(TAG, "onPageFinished:" + url);
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.e(TAG, "onPageStarted:" + url);
        }
    }
}
