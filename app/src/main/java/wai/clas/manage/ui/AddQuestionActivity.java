package wai.clas.manage.ui;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import me.iwf.photopicker.PhotoPicker;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.Utils;
import wai.clas.manage.model.Question;
import wai.clas.manage.model.TotalClass;

/**
 * 添加提问信息
 */
public class AddQuestionActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.title_tv)
    EditText titleTv;
    @Bind(R.id.content_et)
    EditText contentEt;
    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.iv2)
    ImageView iv2;
    @Bind(R.id.button)
    Button button;

    @Override
    public int setLayout() {
        return R.layout.activity_add_question;
    }

    Map<Integer, String> map = new HashMap<>();
    int position = 0;
    TotalClass model;

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
        model = (TotalClass) getIntent().getSerializableExtra("class");
        button.setOnClickListener(view -> {
            String title = titleTv.getText().toString().trim();
            String content = contentEt.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastShort("问题的内容不能为空");
            } else {
                Question question = new Question();
                question.setTitle(title);
                question.setContent(content);
                question.setImg1(map.get(1));
                question.setImg2(map.get(2));
                question.setClas(model);
                question.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            ToastShort("发布成功");
                            setResult(66);
                            finish();
                        } else {
                            ToastShort("发布失败");
                        }
                    }
                });
            }
        });
        iv1.setOnClickListener(view1 -> {
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .start(this, PhotoPicker.REQUEST_CODE);
            position = 0;
        });
        iv2.setOnClickListener(view1 -> {
            PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .start(this, PhotoPicker.REQUEST_CODE);
            position = 1;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                final String[] filePaths = new String[photos.size()];
                for (int i = 0; i < photos.size(); i++) {
                    filePaths[i] = photos.get(i);
                }
                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> files, List<String> urls) {
                        if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                            map.put(position, urls.get(0));
                            if (map.size() == 2 || position == 0) {
                                iv2.setVisibility(View.VISIBLE);
                            } else {
                                iv2.setVisibility(View.GONE);
                            }
                            switch (position) {
                                case 0:
                                    iv1.setImageBitmap(Utils.getBitmapByFile(filePaths[0]));
                                    break;
                                default:
                                    iv2.setImageBitmap(Utils.getBitmapByFile(filePaths[0]));
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(int statuscode, String errormsg) {
                        ToastShort("上传失败，请重试");
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {

                    }
                });

            }
        }
    }


    @Override
    public void initEvents() {

    }
}
