package cc.lijingbo.androidstudy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cc.lijingbo.androidstudy.animation.AnimationMainActivity
import cc.lijingbo.androidstudy.imageloader.ImageLoaderDispatcherActivity
import cc.lijingbo.androidstudy.paypassword.MainActivity
import cc.lijingbo.androidstudy.webview.WebViewActivity

class MainActivity : AppCompatActivity() {

    val context = lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * 自定义 View -支付密码页面
     */
    fun payPassword(view: View?) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent)
    }

    /**
     * 图片加载-加载巨图
     */
    fun loadBigImage(view: View?) {
        val intent = Intent(this, cc.lijingbo.androidstudy.bigimage.ImageLoaderMainActivity::class.java);
        startActivity(intent)
    }

    /**
     * 图片加载-列表展示图片
     */
    fun imageLoader(view: View?) {
        val intent = Intent(this, ImageLoaderDispatcherActivity::class.java);
        startActivity(intent)
    }

    /**
     * 混合开发-js与webview 交互
     */
    fun webviewJs(view: View?) {
        val intent = Intent(this, WebViewActivity::class.java);
        startActivity(intent)
    }

    /**
     * 动画- View 动画和属性动画
     */
    fun animation(view: View?) {
        val animationIntent = Intent(this, AnimationMainActivity::class.java);
        startActivity(animationIntent);
    }
}
