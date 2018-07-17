package cc.lijingbo.androidstudy.animation

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import cc.lijingbo.androidstudy.R
import kotlinx.android.synthetic.main.activity_animation_main.*

class AnimationMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_main)
    }

    fun startAnimationAsXML(view: View?) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.filename);
        btn.startAnimation(animation);
    }

    fun startAnimationAsCode(view: View?) {
        val alphaAnimation = AlphaAnimation(0f, 1f);
        alphaAnimation.duration = 300;
        btn1.startAnimation(alphaAnimation);
    }

    fun valueAnimation2(view: View?) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -100f)
        objectAnimator.start();
    }

    fun valueAnimation3(view: View?) {
        val set = AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btn3, "rotationX", 0f, 360f),
                ObjectAnimator.ofFloat(btn3, "rotationY", 0f, 180f),
                ObjectAnimator.ofFloat(btn3, "rotation", 0f, -90f),
                ObjectAnimator.ofFloat(btn3, "translationX", 0f, 90f),
                ObjectAnimator.ofFloat(btn3, "translationY", 0f, 90f),
                ObjectAnimator.ofFloat(btn3, "scaleX", 1f, 1.5f),
                ObjectAnimator.ofFloat(btn3, "alpha", 1f, 0.25f, 1f)
        )
        set.setDuration(5 * 1000).start();
    }

    fun valueAnimation4(view: View?) {
        ObjectAnimator.ofInt(btn4, "backgroundColor", 1, 1);
        val colorAnimator = ObjectAnimator.ofInt(btn4, "backgroundColor", 1, 2);
        colorAnimator.setDuration(3000)
        colorAnimator.setEvaluator(ArgbEvaluator())
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE
        colorAnimator.start()
    }
}
