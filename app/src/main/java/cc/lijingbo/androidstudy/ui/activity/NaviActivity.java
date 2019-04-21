package cc.lijingbo.androidstudy.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cc.lijingbo.androidstudy.R;
import java.net.URISyntaxException;
import java.util.List;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        findViewById(R.id.navations).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGaodeMap(39.9027077789,116.4271831512,"北京站");
            }
        });
    }

    /**
     * 是否安装了应用
     */
    public static boolean isInstallApk(Context context, String name) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }


    /**
     * 跳转到百度地图
     */
    private void checkBaiduMap(double latitude, double longtitude, String address) {
        if (isInstallApk(this, "com.baidu.BaiduMap")) {// 是否安装了百度地图
            try {
                Intent intent = Intent.parseUri("intent://map/direction?destination=latlng:"
                        + latitude + ","
                        + longtitude + "|name:" + address + // 终点
                        "&mode=driving&" + // 导航路线方式
                        "region=" + //
                        "&src=#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", Intent.URI_ALLOW_UNSAFE);
                startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {// 未安装
            Toast.makeText(this, "您尚未安装百度地图", Toast.LENGTH_LONG)
                    .show();
            Uri uri = Uri
                    .parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    //跳转到高德地图
    private void checkGaodeMap(double latitude, double longtitude, String address) {
        if (isInstallApk(this, "com.autonavi.minimap")) {// 是否安装了高德地图
            try {
                Intent intent = Intent.parseUri("androidamap://navi?sourceApplication=&poiname=" + address + "&lat="
                        + latitude
                        + "&lon="
                        + longtitude + "&dev=0",Intent.URI_ALLOW_UNSAFE);
                this.startActivity(intent); // 启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {// 未安装
            Toast.makeText(this, "您尚未安装高德地图", Toast.LENGTH_LONG)
                    .show();
            Uri uri = Uri
                    .parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
