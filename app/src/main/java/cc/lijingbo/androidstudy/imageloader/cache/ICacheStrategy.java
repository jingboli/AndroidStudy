package cc.lijingbo.androidstudy.imageloader.cache;

/**
 * 类:ICacheStrategy
 * 描述:
 * 优秀 ImageLoader 具备以下功能：
 * 1. 图片的同步加载
 * 2. 图片的异步加载
 * 3. 图片压缩（设置图片显示质量，如 ALPHA_8, RGB_565, ARGB_8888）
 * 4. 内存缓存
 * 5. 硬盘缓存
 * 6. 网络拉取
 * 7. 自定义缓存策略（只使用内存，只使用硬盘，设置内存大小，设置硬盘大小，设置硬盘缓存路径）
 * 8. 缓存策略在 app 初始化的时候进行配置
 * 8. 采用 Builder 模式进行
 *
 * @作者: lijingbo
 * @日期: 2018-07-07 16:21
 */

public interface ICacheStrategy {

    /**
     * 添加缓存
     */
    void addCache();

    /**
     * 删除缓存
     */
    void deleteCache();

    /**
     * 获取缓存
     */
    void getCache();

}
