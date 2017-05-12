package wai.clas.manage.method;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import wai.clas.manage.R;
import wai.clas.manage.model.SCModel;
import wai.clas.manage.model.TotalClass;
import wai.clas.manage.model.UserModel;
import wai.clas.manage.model.key;
import wai.clas.manage.ui.AskManageActivity;
import wai.clas.manage.ui.ClassDetailActivity;
import wai.clas.manage.ui.LoginActivity;
import wai.clas.manage.ui.MainActivity;

/**
 * 3个tab页面在同一个fragment进行控制
 * Created by Finder丶畅畅 on 2017/1/17 21:16
 * QQ群481606175
 */
public class MainFragment extends Fragment {
    int mContent = 0;//当前tab选中
    List<TotalClass> list = new ArrayList<>();
    CommonAdapter<TotalClass> adapter;//自定义adapter
    GridView mainGv;//首页标签罗列

    public static MainFragment newInstance(int content) {
        MainFragment fragment = new MainFragment();
        fragment.mContent = content;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommonAdapter<TotalClass>(MainActivity.admin, list, R.layout.item) {
            @Override
            public void convert(CommonViewHolder holder, TotalClass totalClass, int position) {
                holder.setGliImage(R.id.image, totalClass.getUrl());
                holder.setText(R.id.text, totalClass.getTitle());
            }
        };
    }

    String user_id = Utils.getCache(key.KEY_class_user_id);//获得当前登录的账号的id

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        switch (mContent) {
            case 0://tab选中标签
                view = inflater.inflate(R.layout.frag_main, container, false);
                mainGv = (GridView) view.findViewById(R.id.main_gv);
                BmobQuery<TotalClass> query = new BmobQuery<>();
                query.findObjects(new FindListener<TotalClass>() {//查询出所有的标签
                    @Override
                    public void done(List<TotalClass> lists, BmobException e) {
                        if (e == null) {
                            list = lists;
                            adapter.refresh(lists);
                        }
                    }
                });
                mainGv.setAdapter(adapter);
                mainGv.setOnItemClickListener((parent, v, position, id) ->//跳转到当前课程管理页面
                        Utils.IntentPost(ClassDetailActivity.class, intent -> intent.putExtra("class", list.get(position)))
                );
                break;
            case 1://tab标签选中的我的收藏
                view = inflater.inflate(R.layout.activity_sc_manage, container, false);
                go_login_tv = (TextView) view.findViewById(R.id.go_login_tv);
                sc_lv = (ListView) view.findViewById(R.id.sc_lv);
                sc_srl = (SwipeRefreshLayout) view.findViewById(R.id.sc_srl);
                if (TextUtils.isEmpty(user_id)) {//当前未登录显示的内容
                    go_login_tv.setVisibility(View.VISIBLE);
                    go_login_tv.setOnClickListener(view1 -> Utils.IntentPost(LoginActivity.class));//点击跳转到登录
                } else {
                    go_login_tv.setVisibility(View.GONE);
                    orderModels = new ArrayList<>();
                    pj_adapter = new CommonAdapter<SCModel>(MainActivity.admin, orderModels, R.layout.item_pj) {
                        @Override
                        public void convert(CommonViewHolder holder, SCModel model, int position) {
                            holder.setGliImage(R.id.img_iv, model.getSubj().getUrl());
                            holder.setText(R.id.name_tv, model.getSubj().getTitle());
                            holder.setOnClickListener(R.id.delete_btn, view -> {
                                model.delete(model.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            MainActivity.admin.ToastShort("删除成功");
                                            orderModels.remove(position);
                                            pj_adapter.refresh(orderModels);
                                        } else {
                                            MainActivity.admin.ToastShort("删除失败，请重试");
                                        }
                                    }
                                });
                            });
                        }
                    };
                    sc_lv.setAdapter(pj_adapter);
                    refresh();
                    sc_srl.setOnRefreshListener(() -> {
                        refresh();
                        sc_srl.setRefreshing(false);
                    });
                    sc_lv.setOnItemClickListener((adapterView, v, i, l) -> {//收藏标签点击事件
                        Utils.IntentPost(ClassDetailActivity.class, intent -> intent.putExtra("class", orderModels.get(i).getSubj()));
                    });
                }
                break;
            default://tab选中个人中心
                view = inflater.inflate(R.layout.activity_user_center, container, false);
                no_login_tv = (TextView) view.findViewById(R.id.no_login_tv);
                name_tv = (TextView) view.findViewById(R.id.name_tv);
                ask_manage_tv = (TextView) view.findViewById(R.id.ask_manage_tv);
                exit_btn = (TextView) view.findViewById(R.id.exit_btn);
                change_name_btn = (Button) view.findViewById(R.id.change_name_btn);
                if (TextUtils.isEmpty(user_id)) {//未登录状态
                    no_login_tv.setText("请点击登录");
                    no_login_tv.setOnClickListener(view1 -> Utils.IntentPost(LoginActivity.class));
                    name_tv.setVisibility(View.GONE);
                    change_name_btn.setVisibility(View.GONE);
                } else {//登录状态
                    no_login_tv.setText("昵称");
                    name_tv.setVisibility(View.VISIBLE);
                    change_name_btn.setVisibility(View.VISIBLE);
                    BmobQuery<UserModel> q = new BmobQuery();
                    q.addWhereEqualTo("objectId", Utils.getCache(key.KEY_class_user_id));
                    q.findObjects(new FindListener<UserModel>() {//查询出当前登录用户的用户姓名
                        @Override
                        public void done(List<UserModel> list, BmobException e) {
                            if (e == null) {
                                name_tv.setText(list.get(0).getName());//显示在输入框
                            }
                        }
                    });
                    ask_manage_tv.setOnClickListener(view1 -> Utils.IntentPost(AskManageActivity.class));//跳转当问题管理页面
                    change_name_btn.setOnClickListener(v -> {//修改昵称操作
                        String name = name_tv.getText().toString().trim();
                        if (TextUtils.isEmpty(name)) {
                            MainActivity.admin.ToastShort("昵称不能为空");
                        } else {
                            userModel.setName(name);
                            BmobUser user = BmobUser.getCurrentUser();
                            if (user == null) {//当前未登录用户,重新登录一次（Bmob必须登录以后才可以进行修改操作）
                                BmobUser.loginByAccount(Utils.getCache(key.KEY_class_tel), Utils.getCache(key.KEY_class_pwd), new LogInListener<Object>() {
                                    @Override
                                    public void done(Object o, BmobException e) {
                                        if (e == null) {//登录成功以后执行修改操作
                                            userModel.update(user_id, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        MainActivity.admin.ToastShort("保存成功");
                                                    }
                                                }
                                            });
                                        } else {
                                            MainActivity.admin.ToastShort("请检查网络连接");
                                        }
                                    }
                                });
                            } else {//登录成功以后执行修改操作
                                userModel.update(user_id, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            MainActivity.admin.ToastShort("保存成功");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                exit_btn.setOnClickListener(v -> {//退出登录操作
                    MainActivity.admin.finish();
                    Utils.putCache(key.KEY_class_user_id, "");
                });
                break;
        }
        return view;
    }

    void refresh() {
        BmobQuery<SCModel> bmobQuery = new BmobQuery<>();
        UserModel model = new UserModel();//查询出当前登录账号，所收藏的所有课程
        model.setObjectId(Utils.getCache(key.KEY_class_user_id));
        bmobQuery.include("subj");//关联课程表
        bmobQuery.order("-createdAt");//创建时间倒序查询
        bmobQuery.addWhereEqualTo("user", model);//查询当前账户
        bmobQuery.findObjects(new FindListener<SCModel>() {
            @Override
            public void done(List<SCModel> list, BmobException e) {
                if (e == null) {
                    orderModels = list;
                    pj_adapter.refresh(orderModels);//刷新当前页面显示数据
                }
            }
        });
    }

    List<SCModel> orderModels;
    CommonAdapter<SCModel> pj_adapter;
    UserModel userModel = new UserModel();
    ListView sc_lv;
    TextView go_login_tv;
    TextView no_login_tv;
    TextView ask_manage_tv;
    Button change_name_btn;
    TextView name_tv;
    TextView exit_btn;
    SwipeRefreshLayout sc_srl;
}
