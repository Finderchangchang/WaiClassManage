package wai.clas.manage.method;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 控制首页3个tab切换
 * Created by Finder丶畅畅 on 2017/1/17 21:15
 * QQ群481606175
 */

public class MainAdapter extends FragmentPagerAdapter {
    private String mTitles[] = {"标签", "我的收藏", "个人中心"};

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
