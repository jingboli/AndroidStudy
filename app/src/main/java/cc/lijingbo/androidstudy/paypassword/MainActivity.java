package cc.lijingbo.androidstudy.paypassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.paypassword.dialog.CommonDialog;
import cc.lijingbo.androidstudy.paypassword.dialog.CommonDialog.Builder;
import cc.lijingbo.androidstudy.paypassword.widget.CustomKeyBoard;
import cc.lijingbo.androidstudy.paypassword.widget.KeyBoardEvent;
import cc.lijingbo.androidstudy.paypassword.widget.PassWordEdittext;
import cc.lijingbo.androidstudy.paypassword.widget.PayListener;
import cc.lijingbo.androidstudy.paypassword.widget.TestMain;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    public static final int WHAT = 1000;

    MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_main);
        mHandler = new MyHandler(this);

        TestMain<String,String> testMain = new TestMain<>(111);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void sendMessage(View view) {
        //模拟耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Message message = Message.obtain();
                    message.what = WHAT;
                    new Handler().sendMessage(message);
                }
            }
        }).start();

    }

    static class MyHandler extends Handler {

        WeakReference<MainActivity> weakReference;

        MyHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT:
                    MainActivity mainActivity = weakReference.get();
                    if (mainActivity != null) {
                        Toast.makeText(mainActivity, "Toast", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public void popupDialog(View view) {
        Builder builder = new Builder(MainActivity.this).setView(R.layout.pay_dialog_layout).fromBottom().fullWidth()
                .setCancelable(true);
        final CommonDialog commonDialog = builder.create();
        commonDialog.show();
        ImageView closePopupIv = builder.getView(R.id.close_popup);
        closePopupIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });
        final PassWordEdittext pwet = builder.getView(R.id.password);
        CustomKeyBoard ckb = builder.getView(R.id.customkeyboard);
        pwet.setPayListener(new PayListener() {
            @Override
            public void fullPassword(String trim) {
                Toast.makeText(MainActivity.this, "password->" + trim, Toast.LENGTH_SHORT).show();
            }
        });
        ckb.setCustomKeyBoardEvent(new KeyBoardEvent() {
            @Override
            public void onClick(String number) {
                String string = pwet.getText().toString();
                string += number;
                pwet.setText(string);

            }

            @Override
            public void deletePassword() {
                pwet.deletePassword();
            }

            @Override
            public void clearPassWord() {

            }
        });

    }

    public void circlePoint(View view) {
        Intent i = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(i);
    }
}
