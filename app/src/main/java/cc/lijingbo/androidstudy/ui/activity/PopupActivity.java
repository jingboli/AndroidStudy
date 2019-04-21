package cc.lijingbo.androidstudy.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.ui.dialog.CustomPartShadowPopupView;
import com.lxj.xpopup.XPopup;

public class PopupActivity extends AppCompatActivity {

    private CustomPartShadowPopupView popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        findViewById(R.id.pop_up_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupView == null) {
                    popupView = (CustomPartShadowPopupView) new XPopup.Builder(PopupActivity.this)
                            .atView(v)
                            .asCustom(new CustomPartShadowPopupView(PopupActivity.this));
                }
                popupView.toggle();
            }
        });
    }
}
