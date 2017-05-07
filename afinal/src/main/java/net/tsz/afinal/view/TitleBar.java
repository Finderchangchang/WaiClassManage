package net.tsz.afinal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.R;

/**
 * Created by Administrator on 2016/11/28.
 */

public class TitleBar extends LinearLayout {
    int str_left_iv;
    int str_center_iv;
    int str_right_iv;
    boolean no_bottom_line;//默认显示
    String center_str;//中间字
    String str_center_tv;
    String str_left_tv;
    String str_right_tv;
    ImageView left_iv;
    TextView left_tv;
    ImageView center_iv;
    ImageView right_iv;
    TextView right_tv;
    TextView center_tv;
    RelativeLayout right_rl;
    LeftClick leftClick;
    RightClick rightClick;
    LinearLayout bottom_line_ll;
    RelativeLayout left_rl;

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        str_left_iv = a.getResourceId(R.styleable.TitleBar_left_iv, 0);
        str_center_iv = a.getResourceId(R.styleable.TitleBar_center_iv, 0);
        str_right_iv = a.getResourceId(R.styleable.TitleBar_right_iv, 0);
        str_center_tv = a.getString(R.styleable.TitleBar_center_tv);
        str_left_tv = a.getString(R.styleable.TitleBar_left_tv);
        no_bottom_line = a.getBoolean(R.styleable.TitleBar_no_bottom_line, false);
        str_right_tv = a.getString(R.styleable.TitleBar_right_tv);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.v_title_bar, this);
        left_iv = (ImageView) findViewById(R.id.left_iv);
        left_rl = (RelativeLayout) findViewById(R.id.left_rl);
        center_iv = (ImageView) findViewById(R.id.center_iv);
        right_iv = (ImageView) findViewById(R.id.right_iv);
        center_tv = (TextView) findViewById(R.id.center_tv);
        left_tv = (TextView) findViewById(R.id.left_tv);
        bottom_line_ll = (LinearLayout) findViewById(R.id.bottom_line_ll);
        right_tv = (TextView) findViewById(R.id.right_tv);
        right_rl = (RelativeLayout) findViewById(R.id.right_rl);
        if (!TextUtils.isEmpty(str_right_tv)) {
            right_tv.setVisibility(VISIBLE);
            right_tv.setText(str_right_tv);
        }
        right_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightClick != null) {
                    rightClick.onClick();
                }
            }
        });
        if (!TextUtils.isEmpty(str_left_tv)) {
            left_tv.setText(str_left_tv);
            left_tv.setVisibility(VISIBLE);
            left_iv.setVisibility(GONE);
        } else {
            if (str_left_iv != 0) {
                left_iv.setImageResource(str_left_iv);
            }
            left_tv.setVisibility(GONE);
            left_iv.setVisibility(VISIBLE);
        }
        left_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftClick != null) {
                    leftClick.onClick();
                }
            }
        });

        if (!("").equals(str_center_tv)) {//中间文字显示隐藏
            center_tv.setVisibility(VISIBLE);
            center_tv.setText(str_center_tv);
            center_iv.setVisibility(GONE);
        } else {
            center_tv.setVisibility(GONE);
            center_iv.setVisibility(VISIBLE);
        }

        if (str_right_iv != 0) {
            right_iv.setImageResource(str_right_iv);
            right_iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rightClick != null) {
                        rightClick.onClick();
                    }
                }
            });
        }
        if (no_bottom_line) {
            bottom_line_ll.setVisibility(GONE);
        } else {
            bottom_line_ll.setVisibility(VISIBLE);
        }
    }

    public void setCentertv(String name) {
        center_tv.setText(name);
    }

    public void setRighttv(String name) {
        right_tv.setText(name);
        str_right_tv = name;
    }

    public String getRight_str() {
        return str_right_tv;
    }

    /**
     * 设置右侧文字显示隐藏
     *
     * @param result
     */
    public void setRightClose(boolean result) {
        right_rl.setVisibility(result ? VISIBLE : GONE);
    }

    public TitleBar(Context context) {
        this(context, null);
        init(context);
    }

    public interface LeftClick {
        void onClick();
    }

    public interface RightClick {
        void onClick();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }


    public void setLeftClick(LeftClick leftClick) {
        this.leftClick = leftClick;
    }


    public void setRightClick(RightClick rightClick) {
        this.rightClick = rightClick;
    }


    public String getCenter_str() {
        return center_str;
    }

    public void setCenter_str(String center_str) {
        center_tv.setText(center_str);
    }
}
