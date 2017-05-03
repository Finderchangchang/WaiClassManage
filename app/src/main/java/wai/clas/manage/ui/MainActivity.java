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
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.TotalClass;
import wai.clas.manage.model.key;

public class MainActivity extends BaseActivity {
    public static MainActivity admin;
    @Bind(R.id.go_user_tv)
    TextView goUserTv;
    @Bind(R.id.main_gv)
    GridView mainGv;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    List<TotalClass> list;

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
        data_list = new ArrayList<>();
        BmobQuery<TotalClass> query = new BmobQuery<>();
        query.findObjects(new FindListener<TotalClass>() {
            @Override
            public void done(List<TotalClass> lists, BmobException e) {
                if (e == null) {
                    list = lists;
                    for (TotalClass totalClass : lists) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("image", R.mipmap.ic_launcher);
                        map.put("text", totalClass.getTitle());
                        data_list.add(map);
                        sim_adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        mainGv.setAdapter(sim_adapter);
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
