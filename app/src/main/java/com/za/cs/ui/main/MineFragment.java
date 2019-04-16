package com.za.cs.ui.main;

import android.os.Bundle;
import android.view.View;

import com.min.common.util.AppUtils;
import com.min.common.util.CleanUtils;
import com.min.common.util.FileUtils;
import com.min.common.util.PathUtils;
import com.min.common.widget.CellView;
import com.min.core.base.BaseActivity;
import com.min.core.base.BaseFragment;
import com.min.core.helper.RxHelper;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.za.cs.R;
import com.za.cs.data.DataManager;
import com.za.cs.data.model.UserInfo;
import com.za.cs.helper.UpdateManager;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by minych on 2019/4/16 14:29
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.cv_real_name)
    CellView mRealNameCv;
    @BindView(R.id.cv_user_name)
    CellView mUserNameCv;
    @BindView(R.id.cv_clean_cache)
    CellView mCleanCacheCv;
    @BindView(R.id.cv_update)
    CellView mUpdateCv;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDataToViews();
    }

    private void setDataToViews() {
        UserInfo userInfo = DataManager.getPreferencesHelper().getUserInfo();
        mRealNameCv.setValue(userInfo.realName);
        mUserNameCv.setValue(userInfo.userName);

        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            String sizeStr = FileUtils.getDirSize(PathUtils.getExternalAppCachePath());
            subscriber.onNext(sizeStr);
            subscriber.onCompleted();
        }).compose(bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxHelper.io_main())
                .subscribe(s -> {
                    mCleanCacheCv.setValue(s);
                });

        mUpdateCv.setValue("v" + AppUtils.getAppVersionName());
    }

    @OnClick(R.id.cv_clean_cache)
    void cleanCache() {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            CleanUtils.cleanExternalCache();
            subscriber.onNext("0.00KB");
            subscriber.onCompleted();

        }).delay(1000, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxHelper.io_main())
                .doOnSubscribe(() -> showHudDialog())
                .doOnTerminate(() -> hideHudDialog())
                .subscribe(s -> {
                    mCleanCacheCv.setValue(s);
                });
    }

    @OnClick(R.id.cv_update)
    void checkUpdate() {
        new UpdateManager((BaseActivity) getActivity()).checkUpdate(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

}
