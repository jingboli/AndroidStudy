package cc.lijingbo.androidstudy.webview;

import android.graphics.Paint;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.LogUtils;
import com.just.agentweb.WebListenerManager;

/**
 * 北京中油瑞飞信息技术有限责任公司 研究院 瑞信项目
 * All Rights Reserved
 * 项目:瑞信项目
 * 类:AgentWebSettingsManager
 * 描述:
 * 版本信息：since 2.0
 *
 * @作者: lijingbo
 * @日期: 2018-07-11 11:34
 */

public class AgentWebSettingsManager extends AbsAgentWebSettings {

    private WebSettings mWebSettings;
    private static final String TAG = AbsAgentWebSettings.class.getSimpleName();

    public static AgentWebSettingsManager getInstance() {
        return new AgentWebSettingsManager();
    }

    protected AgentWebSettingsManager() {
    }

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {

    }

    @Override
    public IAgentWebSettings toSetting(WebView webView) {
        this.settings(webView);
        return this;
    }

    private void settings(WebView webView) {
        this.mWebSettings = webView.getSettings();
        this.mWebSettings.setJavaScriptEnabled(true);
        this.mWebSettings.setSupportZoom(true);
        this.mWebSettings.setBuiltInZoomControls(false);
        this.mWebSettings.setSavePassword(false);
//        if(AgentWebUtils.checkNetwork(webView.getContext())) {
//            this.mWebSettings.setCacheMode(-1);
//        } else {
//            this.mWebSettings.setCacheMode(1);
//        }
        if (VERSION.SDK_INT >= 21) {
            this.mWebSettings.setMixedContentMode(0);
            webView.setLayerType(2, (Paint) null);
        } else if (VERSION.SDK_INT >= 19) {
            webView.setLayerType(2, (Paint) null);
        } else if (VERSION.SDK_INT < 19) {
            webView.setLayerType(1, (Paint) null);
        }

        this.mWebSettings.setTextZoom(100);
        this.mWebSettings.setDatabaseEnabled(true);
        this.mWebSettings.setAppCacheEnabled(true);
//        this.mWebSettings.setLoadsImagesAutomatically(true);
        this.mWebSettings.setSupportMultipleWindows(false);
        this.mWebSettings.setBlockNetworkImage(false);
        this.mWebSettings.setAllowFileAccess(true);
        this.mWebSettings.setAllowFileAccessFromFileURLs(false);
        this.mWebSettings.setAllowUniversalAccessFromFileURLs(false);
        this.mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        this.mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        this.mWebSettings.setLoadWithOverviewMode(true);
        this.mWebSettings.setUseWideViewPort(true);
        this.mWebSettings.setDomStorageEnabled(true);
        this.mWebSettings.setNeedInitialFocus(true);
        this.mWebSettings.setDefaultTextEncodingName("utf-8");
        this.mWebSettings.setDefaultFontSize(16);
        this.mWebSettings.setMinimumFontSize(12);
        this.mWebSettings.setGeolocationEnabled(true);
        String dir = AgentWebConfig.getCachePath(webView.getContext());
        LogUtils.i(TAG, "dir:" + dir + "   appcache:" + AgentWebConfig.getCachePath(webView.getContext()));
        this.mWebSettings.setGeolocationDatabasePath(dir);
        this.mWebSettings.setDatabasePath(dir);
        this.mWebSettings.setAppCachePath(dir);
        this.mWebSettings.setAppCacheMaxSize(9223372036854775807L);
        String user_agent = this.mWebSettings.getUserAgentString();
        this.mWebSettings.setUserAgentString(user_agent + " QiXinWebView");
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            this.mWebSettings.setLoadsImagesAutomatically(true);
            //5.0以上的手机对于https和http混合使用需要设置
            this.mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        } else {
            this.mWebSettings.setLoadsImagesAutomatically(false);
        }
    }

    @Override
    public WebSettings getWebSettings() {
        return this.mWebSettings;
    }

    @Override
    public WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient) {
        return super.setWebChromeClient(webview, webChromeClient);
    }

    @Override
    public WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient) {
        return super.setWebViewClient(webView, webViewClient);
    }

}
