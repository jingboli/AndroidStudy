package cc.lijingbo.androidstudy.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.ImageView;
import cc.lijingbo.androidstudy.imageloader_ren.HttpClient_li;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类:ImageLoader
 *
 * @作者: lijingbo
 * @日期: 2018-07-07 17:10
 */

public class ImageLoader {

    private LruCache<String, Bitmap> mMemoryCache;
    private Executor DOWN_EXECUTORS = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final int EXECUTE_BITMAP = 0X11;

    private static Object lock = new Object();

    private ImageLoader() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int memorySize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(memorySize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EXECUTE_BITMAP:
                    String url = (String) msg.obj;
                    Bitmap bitmap = mMemoryCache.get(url);

                    break;
                default:
                    break;
            }

        }
    };

    private static volatile ImageLoader INSTANCE;

    public static ImageLoader getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new ImageLoader();
                }
            }
        }
        return INSTANCE;
    }

    public void bindBitmap(final String url, final ImageView photo) {
        photo.setTag(url);
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap != null) {
            photo.setImageBitmap(bitmap);
            return;
        }
        photo.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                    int oldRight,
                    int oldBottom) {
                final int measuredWidth = photo.getMeasuredWidth();
                final int measuredHeight = photo.getMeasuredHeight();
                photo.removeOnLayoutChangeListener(this);
                DOWN_EXECUTORS.execute(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = loadBitmap(url, measuredWidth, measuredHeight);
                        if (bitmap != null) {
                            if (photo.getTag().equals(url)) {
                                photo.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        photo.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });


    }

    @WorkerThread
    private Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            bitmap = downLoadBitmapFromHttp(url, reqWidth, reqHeight);
        } catch (IOException e) {
            return null;
        }
        if (bitmap != null) {
            mMemoryCache.put(url, bitmap);
            return mMemoryCache.get(url);
        }
        return null;
    }

    @WorkerThread
    private Bitmap downLoadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = HttpClient_li.getInstance().get(url);
            if (inputStream != null) {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                options.inSampleSize = caluteInSampleSize(options, reqWidth, reqHeight);
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(inputStream, null, options);
            } else {
                return null;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    private int caluteInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeigth) {
        int inSampleSize = 1;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outWidth > reqWidth || outHeight > reqHeigth) {
            int halfWidth = outWidth / 2;
            int halfHeigth = outHeight / 2;
            while (halfWidth / inSampleSize > reqWidth || halfHeigth / inSampleSize > reqHeigth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


}
