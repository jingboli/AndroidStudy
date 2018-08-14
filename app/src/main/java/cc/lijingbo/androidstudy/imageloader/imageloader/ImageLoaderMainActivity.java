package cc.lijingbo.androidstudy.imageloader.imageloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import cc.lijingbo.androidstudy.Images;
import cc.lijingbo.androidstudy.R;
import java.util.Arrays;

public class ImageLoaderMainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ImageLoaderAdapter mAdatper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader_main);
        rv = findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdatper = new ImageLoaderAdapter(this, Arrays.asList(Images.imageUrls));
        rv.setAdapter(mAdatper);
    }
}
