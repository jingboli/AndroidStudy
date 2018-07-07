package cc.lijingbo.androidstudy.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 类:ImageLoader
 *
 * @作者: lijingbo
 * @日期: 2018-07-07 17:10
 */

public class ImageLoader {

    private final Context context;
    private final LruCache<String, Bitmap> mMemoryCache;

    private ImageLoader(Context context) {
        this.context = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSzie = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSzie) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

    }

}
