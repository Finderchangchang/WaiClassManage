package wai.clas.manage.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.model.TotalClass;

public class ClassDetailActivity extends BaseActivity {
    TotalClass class_model;

    @Override
    public int setLayout() {
        return R.layout.activity_class_detail;
    }

    @Override
    public void initViews() {
        class_model = (TotalClass) getIntent().getSerializableExtra("class_id");

    }

    @Override
    public void initEvents() {

    }
}
