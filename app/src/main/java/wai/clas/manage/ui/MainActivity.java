package wai.clas.manage.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.CommonAdapter;
import wai.clas.manage.method.CommonViewHolder;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.TotalClass;
import wai.clas.manage.model.key;

public class MainActivity extends BaseActivity {
    public static MainActivity admin;
    @Bind(R.id.go_user_tv)
    TextView goUserTv;
    @Bind(R.id.main_gv)
    GridView mainGv;
    private List<TotalClass> total_list;
    List<TotalClass> list;
    CommonAdapter<TotalClass> adapter;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        admin = this;
    }

    @Override
    public void initEvents() {
        total_list = new ArrayList<>();
        BmobQuery<TotalClass> query = new BmobQuery<>();
        query.findObjects(new FindListener<TotalClass>() {
            @Override
            public void done(List<TotalClass> lists, BmobException e) {
                if (e == null) {
                    list = lists;
                    adapter.refresh(lists);
                }
            }
        });
        adapter = new CommonAdapter<TotalClass>(this, total_list, R.layout.item) {
            @Override
            public void convert(CommonViewHolder holder, TotalClass totalClass, int position) {
                holder.setGliImage(R.id.image, totalClass.getUrl());
                holder.setText(R.id.text, totalClass.getTitle());
            }
        };
        mainGv.setAdapter(adapter);
        mainGv.setOnItemClickListener((parent, view, position, id) ->//跳转到当前课程管理页面
                Utils.IntentPost(ClassDetailActivity.class, intent -> intent.putExtra("class", list.get(position)))
        );
        goUserTv.setOnClickListener(view -> {
            String user_id = Utils.getCache(key.KEY_class_user_id);
            if (TextUtils.isEmpty(user_id)) {
                Utils.IntentPost(LoginActivity.class);
            } else {
                Utils.IntentPost(UserCenterActivity.class);
            }
        });
    }
}
