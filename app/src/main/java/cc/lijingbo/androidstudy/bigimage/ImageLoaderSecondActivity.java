package cc.lijingbo.androidstudy.bigimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.bigimage.view.BigImageView;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoaderSecondActivity extends AppCompatActivity {

    BigImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader_second);
        iv = (BigImageView) findViewById(R.id.iv);
        try {
            iv.setBitmapInputStream(getResources().getAssets().open("bigpicture.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadBigImage(ImageView view) {
        try {
            InputStream inputStream = getResources().getAssets().open("bigpicture.jpg");
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            int measuredHeight = view.getMeasuredHeight();
            int measuredWidth = view.getMeasuredWidth();
            Rect rect = new Rect(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = regionDecoder.decodeRegion(rect, new Options());
            view.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
