package cc.lijingbo.androidstudy.ui.adapter;

import android.support.annotation.Nullable;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.ui.model.bean.CityBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.List;

/**
 * @作者: lijingbo
 * @日期: 2019-04-21 12:42
 */
public class RxListenerAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    /**
     * 优惠推荐
     */
    public static final int TYPE_HEADER_PROMOTION = 2;
    /**
     * 附近优惠
     */
    public static final int TYPE_HEADER_NEARBY = 3;

    public static final int TYPE_FOOTER_NEAYRBY = 4;


    public RxListenerAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_rv_layout);
        addItemType(TYPE_HEADER_PROMOTION, R.layout.item_play_card_cell_header_promotion_layout);
        addItemType(TYPE_HEADER_NEARBY, R.layout.item_play_card_cell_header_nearby_layout);
        addItemType(TYPE_FOOTER_NEAYRBY, R.layout.item_play_card_adapter_footer);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                CityBean cityBean = (CityBean) item;
                helper.setText(R.id.name, cityBean.getName());
                break;
            case TYPE_HEADER_PROMOTION:
//                helper.getView(R.id.promotion_all_tv).setOnClickListener(obtainOnClickListener(helper, item));
                break;
            case TYPE_HEADER_NEARBY:
//                helper.getView(R.id.promotion_change_tv).setOnClickListener(obtainOnClickListener(helper, item));
                break;
            case TYPE_FOOTER_NEAYRBY:
                break;
            default:
                break;
        }
    }
}
