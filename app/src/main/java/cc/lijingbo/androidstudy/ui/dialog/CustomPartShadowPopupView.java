package cc.lijingbo.androidstudy.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cc.lijingbo.androidstudy.R;
import com.lxj.xpopup.impl.PartShadowPopupView;
import java.util.ArrayList;
import java.util.List;

/**
 * @作者: lijingbo
 * @日期: 2019-04-21 21:45
 */
public class CustomPartShadowPopupView extends PartShadowPopupView {

    private Context mContext;

    public CustomPartShadowPopupView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup;
    }

    TextView text;

    @Override
    protected void onCreate() {
        super.onCreate();
        RecyclerView list = findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(mContext));
        List<String> datas = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            datas.add("Hello world " + i);
        }
        MyAdapter myAdapter = new MyAdapter(datas);
        list.setAdapter(myAdapter);
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag", "CustomPartShadowPopupView onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag", "CustomPartShadowPopupView onDismiss");
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<String> datas;

        public MyAdapter(List<String> datas) {
            this.datas = datas;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_view, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            String s = datas.get(i);
            myViewHolder.view.setText(s);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class MyViewHolder extends ViewHolder {

        public TextView view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.test);
        }
    }
}
