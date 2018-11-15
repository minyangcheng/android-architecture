package com.min.sample.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.min.sample.contract.LoginContract;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter extends LoginContract.Presenter {

    @Override
    public void login(String userName, String userPass) {
        Observable.just(1)
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(bindUtilDetach())
                .subscribe(integer -> {
                    LogUtils.d("获取到登录结果.....");
                    getMvpView().loginFail();
                });
    }

}
