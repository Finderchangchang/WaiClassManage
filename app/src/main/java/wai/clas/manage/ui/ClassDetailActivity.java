package wai.clas.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

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
import wai.clas.manage.model.Question;
import wai.clas.manage.model.TotalClass;
import wai.clas.manage.model.key;

public class ClassDetailActivity extends BaseActivity {
    TotalClass class_model;
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.asl_fb)
    FloatingActionButton aslFb;
    @Bind(R.id.title_num_tv)
    TextView titleNumTv;
    @Bind(R.id.main_lv)
    ListView mainLv;
    @Bind(R.id.main_srl)
    SwipeRefreshLayout mainSrl;

    @Override
    public int setLayout() {
        return R.layout.activity_class_detail;
    }

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
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
        class_model = (TotalClass) getIntent().getSerializableExtra("class");
        mainSrl.setOnRefreshListener(() -> refresh());
        mainSrl.setRefreshing(true);
        refresh();
        mainLv.setOnItemClickListener((adapterView, view, i, l) -> {
            Utils.IntentPost(QuestionActivity.class, intent -> intent.putExtra("id", list.get(i)));
        });
        toolbar.setCenter_str(class_model.getTitle());
    }

    List<Question> list = new ArrayList<>();
    CommonAdapter<Question> adapter;

    void refresh() {
        mainSrl.setRefreshing(false);
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

    @Override
    public void initEvents() {
        aslFb.setOnClickListener(view -> {
            String user_id = Utils.getCache(key.KEY_class_user_id);
            if (TextUtils.isEmpty(user_id)) {
                Utils.IntentPost(LoginActivity.class);
            } else {
                Intent intent = new Intent(ClassDetailActivity.this, AddQuestionActivity.class);
                intent.putExtra("class", class_model);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 66:
                refresh();
                break;
        }
    }
}
