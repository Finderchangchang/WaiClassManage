package wai.clas.manage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * BaseActivity声明相关通用方法
 * <p/>
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    int layoutid;
    ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutid = setLayout();
        if (layoutid != 0) {
            setContentView(layoutid);
        }
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }

    public abstract int setLayout();

    public abstract void initViews();

    public abstract void initEvents();

    private Toast toast = null;

    public void ToastShort(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
