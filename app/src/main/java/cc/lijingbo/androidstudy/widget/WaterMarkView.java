package cc.lijingbo.androidstudy.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import cc.lijingbo.androidstudy.UIUtils;

public class WaterMarkView extends View {

    private int widthSize; // 屏幕宽度
    private int heightSize; // 屏幕高度
    Paint mPaint;
    String mark;


    public WaterMarkView(Context context) {
        super(context);
        initSomething();
    }

    public WaterMarkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSomething();
    }

    public WaterMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSomething();
    }

    private void initSomething() {
        mPaint = new Paint();
        // 设置字体颜色
        mPaint.setColor(Color.rgb(128, 128, 128));
        // 防锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT);
        mark = "李静波";
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //paint string

        if (!TextUtils.isEmpty(mark)) {
            if (landScapeOrientation()) {
                // 横屏
                mPaint.setTextSize(UIUtils.dip2px(getContext(), 20));
            } else {
                // 竖屏
                mPaint.setTextSize(UIUtils.dip2px(getContext(), 20));
            }

            int textWidthScreen = (int) mPaint.measureText(mark);
            // 四个汉字,需要四个汉字的宽度
            String thressStr = "四四四四";
            int textWidth = (int) mPaint.measureText(thressStr + mark);
            int thressStrWidth = (int) mPaint.measureText(thressStr);
            canvas.rotate(-15);

            // 每一高度画一行，每行字符串的间距是四个汉字所占据的屏幕的长度，
            // 下一行比上一行的起始位置向后措1个字符串长度，
            // 因此在画的时候需要将起始位置左侧的字符串也要画出来,
            // 每一行与每一行之间的间距是四个汉字所占据的屏幕的长度
            // 起始位置为-（textWidthScreen + widthLean），即一个字符串的所占据屏幕的长度加偏移量
            // 终点高度为（heightSize + widthLean + thressStrWidth），即屏幕高度加四个汉字的宽度加偏移量
            int i = 0;
            // 画布向右倾斜15度后，最底部的一行偏移的宽度
            int widthLean = (int) (heightSize * ((float) Math.sin(Math.toRadians(15)) / Math
                    .sin(Math.toRadians(75))));
            for (int heightTemp = -thressStrWidth; heightTemp < (heightSize + widthLean + thressStrWidth);
                    heightTemp = heightTemp + thressStrWidth) {
                if (i > 0) {
                    for (int widthTemp = -(textWidthScreen + widthLean) + ((int) ((1.0 * i)
                            * textWidthScreen)); widthTemp > -(widthLean + textWidth);
                            widthTemp = widthTemp - textWidth) {
                        canvas.drawText(mark, widthTemp, heightTemp,
                                mPaint);
                    }
                }
                for (int widthTemp = -(textWidthScreen + widthLean) + ((int) ((1.0 * i) * textWidthScreen));
                        widthTemp < (widthSize + textWidth); widthTemp = widthTemp + textWidth) {
                    canvas.drawText(mark, widthTemp, heightTemp, mPaint);
                }
                i++;
            }
        }
    }


    /**
     * 判断横竖屏，横屏返回true，竖屏返回false
     */
    private boolean landScapeOrientation() {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return false;
        }
        return false;
    }
}
