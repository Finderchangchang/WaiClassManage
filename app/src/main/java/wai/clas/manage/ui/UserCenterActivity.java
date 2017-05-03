package wai.clas.manage.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.UserModel;
import wai.clas.manage.model.key;

public class UserCenterActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.name_tv)
    EditText nameTv;
    @Bind(R.id.change_name_btn)
    Button changeNameBtn;
    @Bind(R.id.exit_btn)
    TextView exitBtn;
    String user_id;

    @Override
    public int setLayout() {
        return R.layout.activity_user_center;
    }

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
        user_id = Utils.getCache(key.KEY_class_user_id);
        exitBtn.setOnClickListener(view -> {
            finish();
            MainActivity.admin.finish();
            Utils.putCache(key.KEY_class_user_id, "");
        });
        changeNameBtn.setOnClickListener(view -> {
            String name = nameTv.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastShort("昵称不能为空");
            } else {
                userModel.setName(name);
                BmobUser user = BmobUser.getCurrentUser();
                if (user == null) {
                    BmobUser.loginByAccount(Utils.getCache("user_tel"), Utils.getCache("user_pwd"), new LogInListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                save();
                            } else {
                                ToastShort("请检查网络连接");
                            }
                        }
                    });
                } else {
                    save();
                }
            }
        });
    }

    UserModel userModel = new UserModel();

    void save() {
        userModel.update(user_id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastShort("保存成功");
                }
            }
        });
    }

    @Override
    public void initEvents() {

    }
}
