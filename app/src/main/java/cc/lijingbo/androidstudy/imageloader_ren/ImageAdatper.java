package cc.lijingbo.androidstudy.imageloader_ren;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cc.lijingbo.androidstudy.R;
import java.util.List;

/**
 * @作者: lijingbo
 * @日期: 2018-07-09 15:09
 */

public class ImageAdatper extends RecyclerView.Adapter<ImageViewHolder> {

    Context mContext;
    List<String> mList;

    public ImageAdatper(Context context, List<String> imageList) {
        mContext = context;
        mList = imageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_imageloader, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String url = mList.get(position);
        holder.tv.setText(url);
        ImageLoader.build(mContext).bindBitmap(url, holder.iv);
//        Glide.with(mContext).load(url).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    @Override
//    public void onViewDetachedFromWindow(ImageViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//        holder.tv.setText(null);
//        holder.iv.setImageBitmap(null);
//    }

    @Override
    public void onViewRecycled(ImageViewHolder holder) {
        super.onViewRecycled(holder);
        holder.tv.setText(null);
        holder.iv.setImageBitmap(null);
    }


}
