package cc.lijingbo.androidstudy.imageloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cc.lijingbo.androidstudy.R;

public class MainActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader_main);

        iv = (ImageView) findViewById(R.id.iv);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

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

    public void loadBigImage(View view) {
        loadImage(iv);
    }
}
