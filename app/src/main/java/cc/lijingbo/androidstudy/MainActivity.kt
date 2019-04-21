package cc.lijingbo.androidstudy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cc.lijingbo.androidstudy.animation.AnimationMainActivity
import cc.lijingbo.androidstudy.imageloader.ImageLoaderDispatcherActivity
import cc.lijingbo.androidstudy.paypassword.MainActivity
import cc.lijingbo.androidstudy.ui.NaviActivity
import cc.lijingbo.androidstudy.ui.RvListenerActivity
import cc.lijingbo.androidstudy.ui.WaterMarkActivity
import cc.lijingbo.androidstudy.webview.WebViewActivity

class MainActivity : AppCompatActivity() {
    val tag = "MainActivity";
    val context = lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(tag, "onCreate");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        Log.e(tag, "onStart");
        super.onStart()
    }

    override fun onResume() {
        Log.e(tag, "onResume");
        super.onResume()
    }

    override fun onPause() {
        Log.e(tag, "onPause");
        super.onPause()
    }

    override fun onStop() {
        Log.e(tag, "onStop");
        super.onStop()
    }

    override fun onDestroy() {
        Log.e(tag, "onDestroy");
        super.onDestroy()
    }

    override fun onRestart() {
        Log.e(tag, "onRestart");
        super.onRestart()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.e(tag, "onSaveInstanceState");
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.e(tag, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        Log.e(tag, "onNewIntent");
        super.onNewIntent(intent)
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

    fun dialogClick(view: View?) {
        val builder = AlertDialog.Builder(this);
        builder.setMessage("hello world")
        builder.setNegativeButton("sure", null);
        builder.show()
    }

    fun alphaActivity(view: View?) {
        startActivity(Intent(this, AlphaActivity::class.java))
    }

    fun waterMarkActivity(view: View?) {
        startActivity(Intent(this, WaterMarkActivity::class.java))
    }

    fun rvListenerMethod(view: View?){
        startActivity(Intent(this,RvListenerActivity::class.java))
    }

    fun nativation(view: View?){
        startActivity(Intent(this, NaviActivity::class.java))
    }
}
