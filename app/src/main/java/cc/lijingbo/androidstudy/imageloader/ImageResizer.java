package cc.lijingbo.androidstudy.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.FileDescriptor;

/**
 * 类:ImageResizer
 *
 * @作者: lijingbo
 * @日期: 2018-07-07 17:15
 */

public class ImageResizer {

    public ImageResizer() {
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resid, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resid, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resid, options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    private int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            int halfWidth = outWidth / 2;
            int halfHeigth = outHeight / 2;
            while ((halfHeigth / sampleSize) > reqHeight || (halfWidth / sampleSize) > reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

}
