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

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.ti_indicator)
    TabViewPagerIndicator mTabIndicator;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}
