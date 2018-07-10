package cc.lijingbo.androidstudy.imageloader_ren;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cc.lijingbo.androidstudy.R;

/**
 * @作者: lijingbo
 * @日期: 2018-07-09 15:10
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

    public ImageView iv;
    public TextView tv;

    public ImageViewHolder(View itemView) {
        super(itemView);
        iv = itemView.findViewById(R.id.iv);
        tv = itemView.findViewById(R.id.tv);
    }
}
