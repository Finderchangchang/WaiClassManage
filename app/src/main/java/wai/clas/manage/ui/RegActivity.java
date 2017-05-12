package wai.clas.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.Utils;

/**
 * 注册页面
 */
public class RegActivity extends BaseActivity {
    @Bind(R.id.user_name_et)
    EditText userNameEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.reg_btn)
    Button regBtn;

    @Override
    public int setLayout() {
        return R.layout.activity_reg;
    }

    @Override
    public void initViews() {
        regBtn.setOnClickListener(v -> {
            String name = userNameEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastShort("用户名不能为空");
            } else if (TextUtils.isEmpty(pwd)) {
                ToastShort("密码不能为空");
            } else {
                BmobUser user = new BmobUser();
                user.setUsername(name);
                user.setPassword(pwd);
                user.signUp(new SaveListener<Object>() {
                    @Override//执行注册操作
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent();
                            intent.putExtra("name", name);
                            intent.putExtra("pwd", pwd);
                            setResult(77, intent);
                            finish();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void initEvents() {

    }
}
