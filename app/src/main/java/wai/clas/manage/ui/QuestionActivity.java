package wai.clas.manage.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.view.TitleBar;
import net.tsz.afinal.view.TotalListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.CommonAdapter;
import wai.clas.manage.method.CommonViewHolder;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.AskModel;
import wai.clas.manage.model.Question;
import wai.clas.manage.model.UserModel;
import wai.clas.manage.model.key;

/**
 * 问题详情页面
 */
public class QuestionActivity extends BaseActivity {
    Question model;
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.user_name_tv)
    TextView userNameTv;
    @Bind(R.id.order_time_tv)
    TextView orderTimeTv;
    @Bind(R.id.ask_lv)
    TotalListView askLv;
    @Bind(R.id.ask_et)
    EditText askEt;
    @Bind(R.id.send_ask_btn)
    Button sendAskBtn;
    @Bind(R.id.user_iv1)
    ImageView userIv1;
    @Bind(R.id.user_iv2)
    ImageView userIv2;
    @Bind(R.id.iv_ll)
    LinearLayout iv_ll;
    @Bind(R.id.main_srl)
    SwipeRefreshLayout main_srl;

    @Override
    public int setLayout() {
        return R.layout.activity_question;
    }

    @Override
    public void initViews() {
        model = (Question) getIntent().getSerializableExtra("id");
    }

    @Override
    public void initEvents() {

        adapter = new CommonAdapter<AskModel>(this, list, R.layout.item_ask) {
            @Override
            public void convert(CommonViewHolder holder, AskModel askModel, int position) {
                holder.setText(R.id.name_tv, askModel.getUser().getName());
                holder.setText(R.id.content_tv, askModel.getContent());
                holder.setText(R.id.time_tv, askModel.getCreatedAt());
            }
        };
        askLv.setAdapter(adapter);
        main_srl.setOnRefreshListener(() -> {
            refresh();
            main_srl.setRefreshing(false);
        });
        userNameTv.setText(model.getTitle());
        orderTimeTv.setText(model.getContent());
        toolbar.setLeftClick(() -> finish());
        if (TextUtils.isEmpty(model.getImg1())) {
            iv_ll.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(model.getImg1())
                    .error(R.mipmap.no_img)
                    .into(userIv1);
        }
        if (!TextUtils.isEmpty(model.getImg2())) {
            Glide.with(this)
                    .load(model.getImg1())
                    .error(R.mipmap.no_img)
                    .into(userIv2);
            userIv2.setVisibility(View.VISIBLE);
        }
        sendAskBtn.setOnClickListener(view -> {
            String user_id = Utils.getCache(key.KEY_class_user_id);
            if (TextUtils.isEmpty(user_id)) {
                Utils.IntentPost(LoginActivity.class);
            } else {
                String val = askEt.getText().toString().trim();
                if (TextUtils.isEmpty(val)) {
                    ToastShort("请说些什么吧~");
                } else {
                    AskModel askModel = new AskModel();
                    askModel.setContent(val);
                    UserModel user = new UserModel();
                    user.setObjectId(user_id);
                    askModel.setUser(user);
                    user = new UserModel();
                    user.setObjectId(Utils.getCache(key.KEY_class_user_id));
                    askModel.setQ_user(user);
                    askModel.setQuestion(model);
                    askModel.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                main_srl.setRefreshing(true);
                                refresh();
                                askEt.setText("");
                            }
                        }
                    });
                }
            }
        });
        refresh();
    }

    List<AskModel> list = new ArrayList<>();
    CommonAdapter<AskModel> adapter;

    /**
     * 刷新ask列表
     */
    void refresh() {
        BmobQuery<AskModel> query = new BmobQuery<>();
        query.addWhereEqualTo("question", model);//根据问题进行查询
        query.order("-createdAt");
        query.include("user");//关联user表
        query.findObjects(new FindListener<AskModel>() {
            @Override
            public void done(List<AskModel> lists, BmobException e) {
                if (e == null) {
                    list = lists;
                    adapter.refresh(list);
                }
            }
        });
        main_srl.setRefreshing(false);
    }
}
