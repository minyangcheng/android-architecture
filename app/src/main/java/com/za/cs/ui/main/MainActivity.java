package com.za.cs.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.min.common.widget.tab.TabViewPagerIndicator;
import com.min.core.base.BaseActivity;
import com.za.cs.R;
import com.za.cs.helper.MyFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager mVp;
    private TabViewPagerIndicator mTabIndicator;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initData();

        mVp.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager(), mFragmentList));
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mTabIndicator.setViewPager(mVp);
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new WorkStationFragment());
        mFragmentList.add(new MineFragment());
    }

    private void findViews() {
        mVp = findViewById(R.id.vp);
        mTabIndicator = findViewById(R.id.ti_indicator);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}
