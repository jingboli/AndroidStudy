package cc.lijingbo.androidstudy.imageloader.imageloader_ren;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 北京中油瑞飞信息技术有限责任公司 研究院 瑞信项目
 * All Rights Reserved
 * 项目:瑞信项目
 * 类:Utils
 * 描述:
 * 版本信息：since 2.0
 *
 * @作者: lijingbo
 * @日期: 2018-07-09 11:11
 */

public class Utils {

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
