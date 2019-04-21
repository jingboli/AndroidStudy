package cc.lijingbo.androidstudy.ui.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @作者: lijingbo
 * @日期: 2019-04-21 12:59
 */
public class CityBean implements MultiItemEntity {

    private String name="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
