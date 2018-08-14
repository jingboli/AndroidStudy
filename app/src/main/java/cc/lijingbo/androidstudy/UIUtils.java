package cc.lijingbo.androidstudy;

import android.content.Context;

/**
 * 北京中油瑞飞信息技术有限责任公司 研究院 瑞信项目
 * All Rights Reserved
 * 项目:瑞信项目
 * 类:UIUtils
 * 描述:
 * 版本信息：since 2.0
 *
 * @作者: lijingbo
 * @日期: 2018-08-14 10:17
 */
public class UIUtils {

    /**
     * dip转换px
     */
    public static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

}
