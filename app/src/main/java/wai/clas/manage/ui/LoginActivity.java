package wai.clas.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.key;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.user_name_et)
    EditText userNameEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.reg_btn)
    Button regBtn;

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        regBtn.setOnClickListener(v -> startActivityForResult(new Intent(LoginActivity.this, RegActivity.class), 0));
        loginBtn.setOnClickListener(v -> {
            String name = userNameEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastShort("用户名不能为空");
            } else if (TextUtils.isEmpty(pwd)) {
                ToastShort("密码不能为空");
            } else {
                BmobUser.loginByAccount(name, pwd, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser o, BmobException e) {
                        if (e == null) {//登录成功
                            finish();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(key.KEY_class_user_id, o.getObjectId());
                            map.put(key.KEY_class_tel, name);
                            map.put(key.KEY_class_pwd, pwd);
                            Utils.putCache(map);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 77:
                userNameEt.setText(data.getStringExtra("name"));
                pwdEt.setText(data.getStringExtra("pwd"));
                break;
        }
    }

    @Override
    public void initEvents() {

    }
}
