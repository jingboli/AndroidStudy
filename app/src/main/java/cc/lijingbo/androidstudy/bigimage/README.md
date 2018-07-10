## 加载大图和加载巨图局部
- 在 Android 开发中， Bitmap 是个吃内存大户，稍微操作不当就会 OOM 。虽然现在第三方的图片加载库已经很多，很完善，但是作为一个 Androider 还得知道如何自己进行操作来加载大图。
- 为什么加载图片会很容易造成 OOM 呢，主要是从图片加载到内存说起，假如一个图片的分辨率是 1000\*20000，那么这张图片加载的内存中的大致大小为 1000\*20000\*4  = 80000000 字节，那么就是占用内存为 77 M 左右，这样的话，很容易造成 OOM 。
- 为了不 OOM ，Android 提供了 BitmapFactory.Options 的 inJustDecodeBounds 和 inSimpleSize ，合理使用这些变量可以轻松的加载图片

### inJustDecodeBounds
- 通过把该变量设置为 true ，可以在不加载图片的情况下，拿到图片的分辨率。这时，decodeResource 方法返回的 Bitmap 是 null 
- 代码
```java
        BitmapFactory.Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bigpicture, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
```

### inSimpleSize
- 通过 inJustDecodeBounds 拿到了图片的分辨率，那么通过 ImageView 的宽和高与图片的宽高进行比较，只有当图片的宽和高任意一个都比图片的宽和高都小的时候，计算结束。inSimpleSize 的大小，默认值为 1 ，然后按照 2 的倍数增加，假如 inSimpleSize 为 2，那么图片的宽和高就按照1/2缩放，这样加载到内存就缩小了4倍。 
- 代码
```java
        BitmapFactory.Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bigpicture, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int inSampleSize = 1;
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        if (outHeight > height || outWidth > width) {
            int halfHeight = outHeight / 2;
            int halfWidth = outWidth / 2;
            while ((halfHeight / inSampleSize) >= height || (halfWidth / inSampleSize) >= width) {
                inSampleSize *= 2;
            }
        }
```

### 加载大图
```java
    private void loadImage(ImageView view) {
        BitmapFactory.Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bigpicture, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int inSampleSize = 1;
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        if (outHeight > height || outWidth > width) {
            int halfHeight = outHeight / 2;
            int halfWidth = outWidth / 2;
            while ((halfHeight / inSampleSize) >= height || (halfWidth / inSampleSize) >= width) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bigpicture, options);
        view.setImageBitmap(bitmap);
    }
```
### 展示巨图局部
- 加载巨图，针对上面方式的话，就无法看清楚了。比如清明上河图，这时候就需要使用到另外的一个类 BitmapRegionDecoder ,通过该类可以展示圈定的矩形区域，这样就可以更加清晰的看到局部了。
- 代码
```java
            InputStream inputStream = getResources().getAssets().open("bigpicture.jpg");
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            int measuredHeight = view.getMeasuredHeight();
            int measuredWidth = view.getMeasuredWidth();
            Rect rect = new Rect(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = regionDecoder.decodeRegion(rect, new Options());
            view.setImageBitmap(bitmap);
```
- 以上就可以在 ImageView 中展示图片的局部。
### 滑动显示巨图
- 上面功能实现了展示巨图的局部，但是想要通过滑动显示巨图的其他区域，就需要自定义 View , 重写 onTouchEvent() 方法，根据手指滑动的距离，重新计算 Rect 的区域，来实现加载大图布局。
- 几个要点：
    1. 在 onMeasure 中拿到测量后的大小，设置给 Rect
    2. 在 onTouchEvent() 方法中，计算滑动的距离，然后设置给 Rect
    3. 设置了新的显示区域以后，调用 invalidate() 方法， 请求绘制，这时候会调用 onDraw() 方法
    4. 在 onDraw() 方法中根据 Rect 拿到需要显示的局部 Bitmap， 通过 Canvas 绘制回来。
```java
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
        init();
    }

    private void init() {
        mRect = new Rect();
        paint = new Paint();
        try {
            InputStream inputStream = getResources().getAssets().open("bigpicture.jpg");

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
        mRect.left = 0;
        mRect.right = getMeasuredWidth() + mRect.left;
        mRect.top = 0;
        mRect.bottom = getMeasuredHeight() + mRect.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = bitmapRegionDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
}
```