package com.min.sample.ui.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.min.common.util.ObjectUtils;
import com.min.common.util.ToastUtils;
import com.min.core.base.BaseActivity;
import com.min.sample.R;
import com.min.sample.contract.LoginContract;
import com.min.sample.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MVPActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.et_phone)
    EditText mPhoneEt;
    @BindView(R.id.et_pwd)
    EditText mPwdEt;

    private String mPhone;
    private String mPwd;

    private LoginContract.Presenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }

    private void initView() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvp;
    }

    @OnClick(R.id.btn_login)
    void clickLoginBtn() {
        if (check()) {
            showHudDialog();
            mLoginPresenter.login(mPhone, mPwd);
        }
    }

    private boolean check() {
        mPhone = mPhoneEt.getText().toString();
        mPwd = mPwdEt.getText().toString();
        if (ObjectUtils.isEmpty(mPhone)) {
            ToastUtils.showShort("请输入手机号码");
            return false;
        }
        if (ObjectUtils.isEmpty(mPwd)) {
            ToastUtils.showShort("请输入手机密码");
            return false;
        }
        return true;
    }

    @Override
    public void loginSuccess() {
        hideHudDialog();
        ToastUtils.showShort("登录成功");
        finish();
    }

    @Override
    public void loginFail() {
        hideHudDialog();
        ToastUtils.showShort("登录失败");
    }
}
