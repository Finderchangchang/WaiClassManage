package wai.clas.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.CommonAdapter;
import wai.clas.manage.method.CommonViewHolder;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.Question;
import wai.clas.manage.model.SCModel;
import wai.clas.manage.model.TotalClass;
import wai.clas.manage.model.UserModel;
import wai.clas.manage.model.key;

/**
 * 课程详情管理
 */
public class ClassDetailActivity extends BaseActivity {
    TotalClass class_model;
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.asl_fb)
    ImageView aslFb;
    @Bind(R.id.title_num_tv)
    TextView titleNumTv;
    @Bind(R.id.main_lv)
    ListView mainLv;
    @Bind(R.id.main_srl)
    SwipeRefreshLayout mainSrl;
    @Bind(R.id.search_word_et)
    EditText search_word_et;
    @Bind(R.id.btn)
    Button btn;

    @Override
    public int setLayout() {
        return R.layout.activity_class_detail;
    }

    String right_title = "收藏";
    String user_id;

    @Override
    public void initViews() {
        user_id = Utils.getCache(key.KEY_class_user_id);
        toolbar.setLeftClick(() -> finish());
        class_model = (TotalClass) getIntent().getSerializableExtra("class");
        BmobQuery<SCModel> query = new BmobQuery<SCModel>();
        query.addWhereEqualTo("subj", class_model);
        UserModel userModel = new UserModel();
        userModel.setObjectId(Utils.getCache(key.KEY_class_user_id));
        query.addWhereEqualTo("user", userModel);//判断当前账户是否收藏当前的标签
        query.findObjects(new FindListener<SCModel>() {
            @Override
            public void done(List<SCModel> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        toolbar.setRighttv("已收藏");
                        right_title = "已收藏";
                    } else {
                        toolbar.setRighttv("收藏");
                        right_title = "收藏";
                    }
                }
            }
        });
        toolbar.setRightClick(() -> {//点击收藏操作
            user_id = Utils.getCache(key.KEY_class_user_id);
            if (TextUtils.isEmpty(user_id)) {
                Utils.IntentPost(LoginActivity.class);
            } else {
                if (("已收藏").equals(right_title)) {
                    ToastShort("您已收藏该分类");
                } else {
                    toolbar.setRighttv("已收藏");
                    right_title = "已收藏";
                    SCModel scModel = new SCModel();
                    scModel.setSubj(class_model);
                    scModel.setUser(userModel);
                    scModel.save(new SaveListener<String>() {
                        @Override//执行收藏操作
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                ToastShort("收藏成功");
                            } else {
                                ToastShort("收藏失败");
                                toolbar.setRighttv("收藏");
                                right_title = "收藏";
                            }
                        }
                    });
                }
            }
        });
        aslFb.setOnClickListener(view -> {//点击跳转到添加问题页面
            if (TextUtils.isEmpty(user_id)) {//当前未登录跳转到登录页面
                Utils.IntentPost(LoginActivity.class);
            } else {//已经登录的跳转到添加问题页面
                Intent intent = new Intent(ClassDetailActivity.this, AddQuestionActivity.class);
                intent.putExtra("class", class_model);
                startActivityForResult(intent, 0);
            }
        });
        adapter = new CommonAdapter<Question>(this, list, R.layout.item_question) {
            @Override
            public void convert(CommonViewHolder holder, Question question, int position) {
                holder.setGliImage(R.id.user_iv, question.getImg1());
                holder.setText(R.id.title_tv, question.getTitle());
                if (!TextUtils.isEmpty(question.getContent())) {
                    if (question.getContent().length() > 70) {
                        holder.setText(R.id.content_tv, question.getContent().substring(0, 70) + "...");
                    } else {
                        holder.setText(R.id.content_tv, question.getContent());
                    }
                }
            }
        };
        mainLv.setAdapter(adapter);
        mainSrl.setOnRefreshListener(() -> refresh());//下拉刷新数据
        mainSrl.setRefreshing(true);//初始化加载刷新动画
        refresh();//初始化页面数据
        mainLv.setOnItemClickListener((adapterView, view, i, l) -> {
            Utils.IntentPost(QuestionActivity.class, intent -> intent.putExtra("id", list.get(i)));
        });//点击列表跳转到问题详情页面
        toolbar.setCenter_str(class_model.getTitle());//顶部显示当前标签内容
        btn.setOnClickListener(v -> {//点击查询刷新数据
            refresh();
        });
    }

    List<Question> list = new ArrayList<>();
    CommonAdapter<Question> adapter;

    /**
     * 刷新操作
     */
    void refresh() {
        String word = search_word_et.getText().toString().trim();
        mainSrl.setRefreshing(false);
        if (!TextUtils.isEmpty(word)) {//如果当前输入查询内容不为空，查询出指定的数据并显示
            List<Question> m = new ArrayList<>();
            for (Question model : list) {
                if (model.getTitle().contains(word)) {
                    m.add(model);
                }
            }
            list = m;
            adapter.refresh(m);
            titleNumTv.setText("当前共：" + m.size() + "个问题");
        } else {//查询出当前课程下面的所有问题，并显示出来
            BmobQuery<Question> query = new BmobQuery<>();
            query.addWhereEqualTo("clas", class_model);
            query.order("-createdAt");
            query.findObjects(new FindListener<Question>() {
                @Override
                public void done(List<Question> lists, BmobException e) {
                    if (e == null) {
                        list = lists;
                        adapter.refresh(lists);
                        titleNumTv.setText("当前共：" + lists.size() + "个问题");
                    }
                }
            });
        }
    }

    @Override
    public void initEvents() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 66://问题添加成功回调，执行刷新数据操作
                refresh();
                break;
        }
    }
}
