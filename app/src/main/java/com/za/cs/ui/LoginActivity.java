package com.za.cs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.min.common.util.LogUtils;
import com.min.common.util.StringUtils;
import com.min.common.util.ToastUtils;
import com.min.core.base.BaseActivity;
import com.za.cs.R;
import com.za.cs.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private Button mSubmitBtn;

    private String mUserName;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initViews();
    }

    private void initViews() {
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
        mSubmitBtn.setOnClickListener(this);
    }

    private void findViews() {
        mUserNameEt = findViewById(R.id.et_user_name);
        mPasswordEt = findViewById(R.id.et_password);
        mSubmitBtn = findViewById(R.id.btn_submit);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            submit();
        }
    }

    private void submit() {
        fillData();
        if (check()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
