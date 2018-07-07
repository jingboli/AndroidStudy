package cc.lijingbo.androidstudy.paypassword.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import cc.lijingbo.androidstudy.R;

/**
 * @作者: lijingbo
 * @日期: 2018-05-25 16:14
 */

public class CirclePointPasswordText extends android.support.v7.widget.AppCompatEditText {

    private int circleSize;// 圆点的大小

    private int circleBgColor;//圆点的初始颜色

    private int circleCount; //密碼的數量

    private int circleColor; // 圓點的顏色

    private Paint mPaint;

    private static final int DEFAULT_CIRCLE_BG_COLOR = Color.parseColor("#d1d2d6");

    private static final int DEFAULT_CIRCLE_SIZE = 10;

    private static final int DEFAULT_CIRCLE_COLOR = Color.parseColor("#40E0D0");
    private int itemDistance;
    private int itemHeight;
    private int mCurrentStringLength;
    PayListener mListener;

    public CirclePointPasswordText(Context context) {
        this(context, null);
    }


    public CirclePointPasswordText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public CirclePointPasswordText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackground(null);
        setEnabled(false);
        initPaint();
        initAttrs(context, attrs);
    }

    private int dp2px(int paramInt) {
        return (int) (0.5D + getDensity() * paramInt);

    }

    public float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }


    // 初始化 paint
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.circlePointPasswordText);
        circleSize = (int) ta
                .getDimension(R.styleable.circlePointPasswordText_circleSize, dp2px(DEFAULT_CIRCLE_SIZE));
        circleBgColor = ta.getColor(R.styleable.circlePointPasswordText_circlrBg, DEFAULT_CIRCLE_BG_COLOR);

        circleCount = (int) ta.getDimension(R.styleable.circlePointPasswordText_circleCount, 6);

        circleColor = ta.getColor(R.styleable.circlePointPasswordText_circleColor, DEFAULT_CIRCLE_COLOR);
        ta.recycle();
    }

    private void initSomeData() {
        int width = getWidth();
        itemHeight = getHeight() / 2;
        itemDistance = width / (circleCount + 1);
        if (itemDistance < circleSize) {
            circleSize = itemDistance - dp2px(2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSomeData();
        drawBgCircle(canvas);
        drawInputCircle(canvas);
    }

    //画背景圆点
    private void drawBgCircle(Canvas canvas) {
        mPaint.setColor(circleBgColor);
        for (int i = 0; i < circleCount; i++) {
            canvas.drawCircle(itemDistance * (i + 1), itemHeight, circleSize, mPaint);
        }
    }

    //画输入密码后的小圆点
    private void drawInputCircle(Canvas canvas) {
        mPaint.setColor(circleColor);
        for (int i = 0; i < mCurrentStringLength; i++) {
            canvas.drawCircle(itemDistance * (i + 1), itemHeight, circleSize, mPaint);
        }
    }

    //监听文字输入的变动
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        mCurrentStringLength = text.toString().length();
        invalidate();
        if (mListener != null) {
            if (mCurrentStringLength == circleCount) {
                mListener.fullPassword(text.toString());
            }
        }
    }


    public void setPayPassWordListener(PayListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }

    public void deletePassWord() {
        String trim = getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            String newString = trim.substring(0, trim.length() - 1);
            setText(newString);
        }
    }

    public void clearPassWord() {
        setText("");
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        int length = text.toString().trim().length();
        if (length <= circleCount) {
            super.setText(text, type);
        }
    }
}
