package cc.lijingbo.androidstudy.imageloader.imageloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.imageloader.ImageViewHolder;
import java.util.List;

/**
 * @作者: lijingbo
 * @日期: 2018-07-10 16:36
 */

public class ImageLoaderAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    Context mContext;
    List<String> mList;

    public ImageLoaderAdapter(Context context, List<String> imageList) {
        mContext = context;
        mList = imageList;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_imageloader, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String url = mList.get(position);
        holder.tv.setText(url);
        ImageLoader.getINSTANCE().bindBitmap(url, holder.iv);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
