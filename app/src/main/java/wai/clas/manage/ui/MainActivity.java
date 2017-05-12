package wai.clas.manage.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;
import wai.clas.manage.method.MainAdapter;

public class MainActivity extends BaseActivity {
    public static MainActivity admin;

    private MainAdapter mAdapter;
    @Bind(R.id.main_vp)
    ViewPager mPager;
    @Bind(R.id.main_tab)
    TabLayout bottom_tab;

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
        mAdapter = new MainAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(3);
        bottom_tab.setupWithViewPager(mPager);
    }
}
