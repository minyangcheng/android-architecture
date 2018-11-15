package com.min.sample.ui.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.min.common.widget.CenterTitleToolbar;
import com.min.core.base.BaseActivity;
import com.min.sample.R;
import com.min.sample.contract.LoginContract;
import com.min.sample.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
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
        initToolbar(mToolbar);
        mToolbar.setTitle("登录");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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
