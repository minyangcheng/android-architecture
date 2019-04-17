package com.min.sample.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.min.common.widget.tab.TabViewPagerIndicator;
import com.min.core.base.BaseActivity;
import com.min.sample.R;
import com.min.sample.ui.fragment.ContentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TabViewPagerActivity extends BaseActivity {

    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.ti_indicator)
    TabViewPagerIndicator mTabIndicator;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentList=new ArrayList<>();
        mFragmentList.add(ContentFragment.newInstance("0"));
        mFragmentList.add(ContentFragment.newInstance("1"));
        mFragmentList.add(ContentFragment.newInstance("2"));
        mFragmentList.add(ContentFragment.newInstance("3"));

        MyFragmentPageAdapter adapter=new MyFragmentPageAdapter(getSupportFragmentManager(),mFragmentList);
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mTabIndicator.setViewPager(mVp);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tab_view_pager;
    }

    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.mFragmentList=fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}
