package cc.lijingbo.androidstudy.paypassword.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.lijingbo.androidstudy.R;

/**
 * @作者: lijingbo
 * @日期: 2018-05-14 18:55
 */

public class CustomKeyBoard extends LinearLayout implements OnClickListener {

    private Context mContext;
    private KeyBoardEvent mEvent;
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private TextView six;
    private TextView seven;
    private TextView eight;
    private TextView night;
    private TextView zero;
    private ImageView delete;
    private View view;

    public CustomKeyBoard(Context context) {
        super(context);
        mContext = context;
        view = inflate(context, R.layout.layout_pay_custom_keyboard, this);
        initView();
    }

    public CustomKeyBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        view = inflate(context, R.layout.layout_pay_custom_keyboard, this);
        initView();
    }

    public CustomKeyBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        view = inflate(context, R.layout.layout_pay_custom_keyboard, this);
        initView();
    }

    private void initView() {
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        six = view.findViewById(R.id.six);
        seven = view.findViewById(R.id.seven);
        eight = view.findViewById(R.id.eight);
        night = view.findViewById(R.id.night);
        zero = view.findViewById(R.id.zero);
        delete = view.findViewById(R.id.delete);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        night.setOnClickListener(this);
        zero.setOnClickListener(this);
        delete.setOnClickListener(this);
    }


    public void setCustomKeyBoardEvent(KeyBoardEvent event) {
        if (event != null) {
            mEvent = event;
        }
    }

    @Override
    public void onClick(View v) {
        if (mEvent != null) {
            if (v instanceof ImageView) {
                mEvent.deletePassword();
            } else if (v instanceof TextView) {
                String string = ((TextView) v).getText().toString();
                mEvent.onClick(string);
            }
        }
    }
}
