package cc.lijingbo.androidstudy.imageloader.imageloader_ren;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.ImageView;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.imageloader.HttpClient_li;
import cc.lijingbo.androidstudy.imageloader.imageloader.cache.DiskLruCache;
import cc.lijingbo.androidstudy.imageloader.imageloader.cache.DiskLruCache.Editor;
import cc.lijingbo.androidstudy.imageloader.imageloader.cache.DiskLruCache.Snapshot;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者: lijingbo
 * @日期: 2018-07-09 09:05
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static final int MESSAGE_POST_RESULT = 1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;

    private static final long KEEP_ALIVE = 10L;

    private static final int TAG_KEY_URI = R.id.imageloader_uri;

    private static final long DISK_CACHE_SIZE = 1024 * 1014 * 50;

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int DISK_CACHE_INDEX = 0;

    private final LruCache<String, Bitmap> mMemoryCache;

    private DiskLruCache mDiskCache = null;

    private boolean mIsDiskLruCacheCreated = false;

    private ImageResizer mImageResizer;

    /**
     * 线程创建工厂
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    /**
     * 线程池
     */
    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), sThreadFactory);

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    // 在 UI 线程，根据 view 的 TAG 与插入的值是否一致，来判断是否需要显示还是已经被回收服用了
                    LoaderResult lr = (LoaderResult) msg.obj;
                    ImageView imageView = lr.getmImageView();
                    String url = (String) imageView.getTag(TAG_KEY_URI);
                    String uri = lr.getUri();
                    if (url.equals(uri)) {
                        imageView.setImageBitmap(lr.getBitmap());
                    } else {
                        Log.w(TAG, "set image bitmap , but url has changed,ingored!");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Context mContext;

    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mImageResizer = new ImageResizer();
    }

    /**
     * 入口
     */
    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    private void addBitmapToMemoryCache(String key, Bitmap value) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, value);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    public void bindBitmap(String uri, ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    public void bindBitmap(final String uri, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, uri);
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        if (reqWidth == 0 || reqHeight == 0) {
            imageView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                        int oldRight,
                        int oldBottom) {
                    final int measuredWidth = imageView.getMeasuredWidth();
                    final int measuredHeight = imageView.getMeasuredHeight();
                    imageView.removeOnLayoutChangeListener(this);
                    executeRunnable(imageView, uri, measuredWidth, measuredHeight);
                }
            });
        } else {
            executeRunnable(imageView, uri, reqWidth, reqHeight);
        }
    }

    private void executeRunnable(final ImageView imageView, final String uri, final int reqWidth, final int reqHeight) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult loaderResult = new LoaderResult(imageView, uri, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, loaderResult).sendToTarget();
                }
            }
        });
    }

    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            bitmap = downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemCache(String url) {
        String key = hashKeyFormUrl(url);
        return getBitmapFromMemCache(key);
    }

    private String hashKeyFormUrl(String url) {
        String cacheKey;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            cacheKey = bytesToHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private Bitmap loadBitmapFromDiskCache(String uri, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from ui thread,it's not recommended!");
        }
        if (mDiskCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(uri);
        Snapshot snapshot = mDiskCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fd = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSampleBitmapFromFileDescriptor(fd, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String uri, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new RuntimeException("can not visit network from UI Thread.");
        }
        if (mDiskCache == null) {
            return null;
        }
        String key = hashKeyFormUrl(uri);
        Editor edit = mDiskCache.edit(key);
        if (edit != null) {
            OutputStream outputStream = edit.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(uri, outputStream)) {
                edit.commit();
            } else {
                edit.abort();
            }
            mDiskCache.flush();
        }
        return loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
    }

    private boolean downloadUrlToStream(String uri, OutputStream outputStream) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            InputStream inputStream = HttpClient_li.getInstance().get(uri);
            bufferedInputStream = new BufferedInputStream(inputStream,
                    IO_BUFFER_SIZE);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int b;
            while ((b = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.close(bufferedInputStream);
            Utils.close(bufferedOutputStream);
        }
        return false;
    }


    private Bitmap downloadBitmapFromUrl(String uri) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            Utils.close(bufferedInputStream);
        }
        return bitmap;
    }


    private long getUsableSpace(File diskCacheDir) {
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            return diskCacheDir.getUsableSpace();
        }
        StatFs statFs = new StatFs(diskCacheDir.getPath());
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

    private File getDiskCacheDir(Context mContext, String bitmap) {
        String cachePath;
        boolean externalAvaliable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (externalAvaliable) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + bitmap);
    }

    public void stopLoadBitmap() {

    }

    public void startLoadBitmap() {

    }

    private static class LoaderResult {

        private ImageView mImageView;
        private String uri;
        private Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            mImageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }

        public ImageView getmImageView() {
            return mImageView;
        }

        public void setmImageView(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

    }

}
