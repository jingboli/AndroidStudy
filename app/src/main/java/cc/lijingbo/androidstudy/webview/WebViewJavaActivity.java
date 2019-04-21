package cc.lijingbo.androidstudy.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import cc.lijingbo.androidstudy.R;

public class WebViewJavaActivity extends AppCompatActivity {

    private static final String TAG = "WebViewJavaActivity";
    WebView mWebView;
    RelativeLayout mLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new MyWebViewClient());
        WebView.setWebContentsDebuggingEnabled(true);
        mLayout = findViewById(R.id.layout);
        initWebview();
    }

    private void initWebview() {
        mWebView.loadUrl("http://sydj.cnpc.com.cn/mobile");
    }

    public void loadUrl(View view) {
        initWebview();
    }


    class MyWebViewClient extends WebViewClient {


        MyWebViewClient() {
        }

        @RequiresApi(api = VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }


        // PAI>=21
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        // API<21
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }


        /**
         * 创建一个WebViewClient,重写onPageStarted和onPageFinished
         * onPageStarted中启动一个计时器,到达设置时间后利用handle发送消息给activity执行超时后的动作.
         */
        @Override
        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(TAG, "onPageStarted" + System.currentTimeMillis() + ", url:" + url);
        }

        /**
         * onPageFinished指页面加载完成,完成后取消计时器
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "onPageFinished" + System.currentTimeMillis() + ", url:" + url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
}
