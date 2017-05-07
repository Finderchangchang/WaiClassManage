package wai.clas.manage.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.CommonAdapter;
import wai.clas.manage.method.CommonViewHolder;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.SCModel;
import wai.clas.manage.model.SCModel;
import wai.clas.manage.model.UserModel;
import wai.clas.manage.model.key;

/**
 * 收藏管理
 */
public class ScManageActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.main_lv)
    ListView mainLv;
    List<SCModel> orderModels;
    CommonAdapter<SCModel> pj_adapter;

    @Override
    public int setLayout() {
        return R.layout.activity_sc_manage;
    }

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
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
                                ToastShort("删除成功");
                                orderModels.remove(position);
                                pj_adapter.refresh(orderModels);
                            } else {
                                ToastShort("删除失败，请重试");
                            }
                        }
                    });
                });
            }
        };
        mainLv.setAdapter(pj_adapter);
        BmobQuery<SCModel> bmobQuery = new BmobQuery<>();
        UserModel model = new UserModel();
        model.setObjectId(Utils.getCache(key.KEY_class_user_id));
        bmobQuery.include("subj");
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereEqualTo("user", model);//当前账户
        bmobQuery.findObjects(new FindListener<SCModel>() {
            @Override
            public void done(List<SCModel> list, BmobException e) {
                if (e == null) {
                    orderModels = list;
                    pj_adapter.refresh(orderModels);
                }
            }
        });
        mainLv.setOnItemClickListener((adapterView, view, i, l) -> {
            Utils.IntentPost(ClassDetailActivity.class, intent -> intent.putExtra("id", orderModels.get(i)));
        });
    }

    @Override
    public void initEvents() {

    }
}
