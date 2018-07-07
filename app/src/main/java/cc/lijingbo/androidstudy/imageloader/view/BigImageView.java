package cc.lijingbo.androidstudy.imageloader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描述: 显示大图用的，可以滑动
 *
 * @作者: lijingbo
 * @日期: 2018-07-05 22:11
 */

public class BigImageView extends View {

    private int slideX = 0;
    private int slideY = 0;
    private BitmapRegionDecoder bitmapRegionDecoder;
    private Paint paint;
    private int mImageWidth;
    private int mImageHeight;
    private Options options;
    private Rect mRect;

    public BigImageView(Context context) {
        this(context, null);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBitmapInputStream(InputStream inputStream) {
        mRect = new Rect();
        paint = new Paint();
        try {
            options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            mImageWidth = options.outWidth;
            mImageHeight = options.outHeight;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestLayout();
    }


    float downX = 0;
    float downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX();
                float moveY = event.getRawY();
                slideX = (int) (moveX - downX);
                slideY = (int) (moveY - downY);
                if (mImageWidth > getWidth()) {
                    mRect.offset(-slideX, 0);
                    if (mRect.right > mImageWidth) {
                        mRect.right = mImageWidth;
                        mRect.left = mRect.right - getWidth();
                    }
                    if (mRect.left < 0) {
                        mRect.left = 0;
                        mRect.right = getWidth();
                    }
                    invalidate();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -slideY);
                    if (mRect.bottom > mImageHeight) {
                        mRect.bottom = mImageHeight;
                        mRect.top = mRect.bottom - getHeight();
                    }
                    if (mRect.top < 0) {
                        mRect.top = 0;
                        mRect.bottom = getHeight();
                    }
                    invalidate();
                }
                downX = moveX;
                downY = moveY;
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRect != null) {
            mRect.left = 0;
            mRect.right = getMeasuredWidth() + mRect.left;
            mRect.top = 0;
            mRect.bottom = getMeasuredHeight() + mRect.top;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmapRegionDecoder != null) {
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(mRect, options);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

    }
}
