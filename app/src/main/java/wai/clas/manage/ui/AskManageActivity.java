package wai.clas.manage.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

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
import wai.clas.manage.model.AskModel;
import wai.clas.manage.model.Question;
import wai.clas.manage.model.UserModel;
import wai.clas.manage.model.key;

/**
 * 提问管理
 */
public class AskManageActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.main_lv)
    ListView mainLv;
    List<AskModel> orderModels;
    CommonAdapter<AskModel> pj_adapter;

    @Override
    public int setLayout() {
        return R.layout.activity_ask_manage;
    }

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
        orderModels = new ArrayList<>();
        pj_adapter = new CommonAdapter<AskModel>(this, orderModels, R.layout.item_question) {
            @Override
            public void convert(CommonViewHolder holder, AskModel question, int position) {
                if (TextUtils.isEmpty(question.getQuestion().getImg1())) {
                    holder.setGliImage(R.id.user_iv, question.getQuestion().getImg1());
                } else {
                    holder.setGliImage(R.id.user_iv, question.getQuestion().getImg2());
                }
                holder.setText(R.id.title_tv, question.getContent() + "  回复了您");
                if (!TextUtils.isEmpty(question.getContent())) {
                    if (question.getContent().length() > 70) {
                        holder.setText(R.id.content_tv, question.getContent().substring(0, 70) + "...");
                    } else {
                        holder.setText(R.id.content_tv, question.getContent());
                    }
                }
            }
        };
        mainLv.setAdapter(pj_adapter);
        BmobQuery<AskModel> bmobQuery = new BmobQuery<>();
        bmobQuery.include("user,question");
        bmobQuery.order("-createdAt");
        UserModel model = new UserModel();
        model.setObjectId(Utils.getCache(key.KEY_class_user_id));
        bmobQuery.addWhereEqualTo("q_user", model);//当前账户
        bmobQuery.findObjects(new FindListener<AskModel>() {
            @Override
            public void done(List<AskModel> list, BmobException e) {
                if (e == null) {
                    orderModels = list;
                    pj_adapter.refresh(orderModels);
                }
            }
        });
        mainLv.setOnItemClickListener((adapterView, view, i, l) -> {
            Utils.IntentPost(QuestionActivity.class, intent -> intent.putExtra("id", orderModels.get(i)));
        });
    }

    @Override
    public void initEvents() {

    }
}
