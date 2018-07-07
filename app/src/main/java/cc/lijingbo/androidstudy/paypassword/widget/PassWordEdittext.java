package cc.lijingbo.androidstudy.paypassword.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import cc.lijingbo.androidstudy.R;

/**
 * 含有输入框的支付密码页面
 *
 * @作者: lijingbo
 * @日期: 2018-05-13 17:30
 */

public class PassWordEdittext extends android.support.v7.widget.AppCompatEditText {

    private Paint mPaint;
    private int bgDefaultColor = Color.parseColor("#d1d2d6");
    private int defaultNumber = 6;
    //背景颜色
    private int mBgColor;
    //背景圆角半径
    private int mBgRectRadio = 0;
    //背景边框大小
    private int mBgRectSize;
    //背景边框颜色
    private int mBgRectColor;

    // 分割线
    // 分割线颜色
    private int mDividerColor;
    // 分割线宽度
    private int mDividerSize;

    private int mNumber;

    private int mPasswordColor;
    private int mPasswordSize;

    private int mItemWidth;

    private PayListener mListener;

    public PassWordEdittext(Context context) {
        this(context, null);
    }

    public PassWordEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrsSet(context, attrs);
        initPaint();
        setBackground(null);
        setFocusable(false);
        setEnabled(false);
    }


    /**
     * 获取自定义属性
     */
    private void initAttrsSet(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PassWordEdittext);
        mBgColor = ta.getColor(R.styleable.PassWordEdittext_bgXColor, bgDefaultColor);
        mBgRectColor = ta.getColor(R.styleable.PassWordEdittext_bgRectColor, bgDefaultColor);
        mBgRectSize = (int) ta.getDimension(R.styleable.PassWordEdittext_bgRectSize, dip2px(1));
        mBgRectRadio = (int) ta.getDimension(R.styleable.PassWordEdittext_bgRadius, 0);

        mDividerColor = ta.getColor(R.styleable.PassWordEdittext_dividerColor, bgDefaultColor);
        mDividerSize = (int) ta.getDimension(R.styleable.PassWordEdittext_dividerSize, dip2px(1));

        mNumber = (int) ta.getDimension(R.styleable.PassWordEdittext_passWordNumber, defaultNumber);

        mPasswordColor = ta.getColor(R.styleable.PassWordEdittext_passWordColor, bgDefaultColor);

        mPasswordSize = (int) ta.getDimension(R.styleable.PassWordEdittext_passWordPointSize, dip2px(4));
        ta.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mItemWidth = (getWidth() - mBgRectSize * 2 - (mNumber - 1) * mDividerSize) / mNumber;
        drawBg(canvas);
        drawDivider(canvas);
        drawPasswordPoint(canvas);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        int length = text.toString().length();
        invalidate();
        if (mListener != null) {
            if (length == mNumber) {
                mListener.fullPassword(text.toString());
            }
        }
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {
        Log.e("TAG", "drawBg");
        float left = mBgRectSize;
        float top = mBgRectSize;
        float right = getWidth() - mBgRectSize;
        float bottom = getHeight() - mBgRectSize;
        RectF rectF = new RectF(left, top, right, bottom);

        mPaint.setColor(mBgColor);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(mBgRectSize);

        if (mBgRectRadio == 0) {
            canvas.drawRect(rectF, mPaint);
        } else {
            canvas.drawRoundRect(rectF, mBgRectRadio, mBgRectRadio, mPaint);
        }
    }


    /**
     * 绘制分割线
     */
    private void drawDivider(Canvas canvas) {
        Log.e("TAG", "drawDivider");
        mPaint.setColor(mDividerColor);
        mPaint.setStrokeWidth(mDividerSize);
        for (int i = 1; i < mNumber; i++) {
            int startX = mBgRectSize + mItemWidth * i + mDividerSize * (i - 1);
            int startY = mBgRectSize;
            int endX = startX;
            int endY = getHeight() - mBgRectSize;
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }
    }

    private void drawPasswordPoint(Canvas canvas) {
        Log.e("TAG", "drawPasswordPoint");
        mPaint.setColor(mPasswordColor);
        mPaint.setStyle(Style.FILL);

        String password = getText().toString().trim();
        int passwordLength = password.length();
        for (int i = 0; i < passwordLength; i++) {
            int cy = getHeight() / 2;
            int cx = mBgRectSize + mItemWidth * i + mDividerSize * i + mItemWidth / 2;
            canvas.drawCircle(cx, cy, mPasswordSize, mPaint);
        }
    }

    public void deletePassword() {
        String trim = getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            String substring = trim.substring(0, trim.length() - 1);
            setText(substring);
        }
    }

    public void clearPassWord() {
        setText("");
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        int length = text.toString().trim().length();
        if (length <= mNumber) {
            super.setText(text, type);
        }

    }

    public void setPayListener(PayListener event) {
        if (event != null) {
            this.mListener = event;
        }
    }


}
