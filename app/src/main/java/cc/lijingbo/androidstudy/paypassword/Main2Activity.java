package cc.lijingbo.androidstudy.paypassword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.paypassword.widget.CirclePointPasswordText;
import cc.lijingbo.androidstudy.paypassword.widget.CustomKeyBoard;
import cc.lijingbo.androidstudy.paypassword.widget.KeyBoardEvent;
import cc.lijingbo.androidstudy.paypassword.widget.PayListener;

public class Main2Activity extends AppCompatActivity {

    private CirclePointPasswordText mCirclePoint;
    private CustomKeyBoard mKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_main2);
        mCirclePoint = findViewById(R.id.circlePoint);
        mKeyBoard = findViewById(R.id.keyboard);

        mCirclePoint.setPayPassWordListener(new PayListener() {
            @Override
            public void fullPassword(String trim) {
                Toast.makeText(Main2Activity.this, trim, Toast.LENGTH_SHORT).show();
            }
        });

        mKeyBoard.setCustomKeyBoardEvent(new KeyBoardEvent() {
            @Override
            public void onClick(String number) {
                String text = mCirclePoint.getText().toString();
                text += number;
                mCirclePoint.setText(text);
            }

            @Override
            public void deletePassword() {
                mCirclePoint.deletePassWord();
            }

            @Override
            public void clearPassWord() {
                mCirclePoint.clearPassWord();
            }
        });
    }
}
