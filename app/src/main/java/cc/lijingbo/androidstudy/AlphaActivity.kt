package cc.lijingbo.androidstudy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class AlphaActivity : AppCompatActivity() {
    val tag = "AlphaActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(tag, "onCreate");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpha)
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
}
