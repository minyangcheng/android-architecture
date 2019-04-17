package com.za.cs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.min.common.util.StringUtils;
import com.min.common.util.ToastUtils;
import com.min.core.base.BaseActivity;
import com.min.core.helper.CSRxHelper;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.za.cs.R;
import com.za.cs.data.DataManager;
import com.za.cs.data.model.UserInfo;
import com.za.cs.helper.LoginHelper;
import com.za.cs.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action0;
import rx.functions.Action1;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_user_name)
    EditText mUserNameEt;
    @BindView(R.id.et_password)
    EditText mPasswordEt;

    private String mUserName;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mUserNameEt.setText(DataManager.getPreferencesHelper().getUserName());
        mPasswordEt.setText(DataManager.getPreferencesHelper().getPassword());
        mPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        fillData();
        if (check()) {
            DataManager.getMobileService()
                    .login(mUserName, mPassword)
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(CSRxHelper.handleServerResult())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            showHudDialog();
                        }
                    })
                    .doOnTerminate(new Action0() {
                        @Override
                        public void call() {
                            hideHudDialog();
                        }
                    })
                    .subscribe(new Action1<UserInfo>() {
                        @Override
                        public void call(UserInfo userInfo) {
                            handleLoginSuccess(userInfo);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            CSRxHelper.handlerError(throwable);
                        }
                    });
        }
    }

    private void handleLoginSuccess(UserInfo userInfo) {
        LoginHelper.login(mUserName, mPassword, userInfo);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean check() {
        if (StringUtils.isTrimEmpty(mUserName)) {
            ToastUtils.showShort("请输入用户名");
            return false;
        }
        if (StringUtils.isTrimEmpty(mPassword)) {
            ToastUtils.showShort("请输入密码");
            return false;
        }
        return true;
    }

    private void fillData() {
        mUserName = mUserNameEt.getText().toString();
        mPassword = mPasswordEt.getText().toString();
    }

}
