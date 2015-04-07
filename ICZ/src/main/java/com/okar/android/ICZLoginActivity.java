package com.okar.android;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/3/16.
 */
public class ICZLoginActivity extends BaseActivity{

    @InjectView(R.id.icz_login_zh_et)
    private EditText zhEt;

    @InjectView(R.id.icz_login_mm_et)
    private EditText mmEt;

    @InjectView(R.id.icz_login_btn)
    Button loginBtn;

    @InjectView(R.id.icz_login_to_zc)
    Button toRegisterBtn;

    int zhTextCount, mmTextCount;

    @Override
    protected void init() {
        setContentView(R.layout.activity_icz_login);
        registerEditTextListener();
        initLoginBtn();
        loginBtn.setOnClickListener(this);
        toRegisterBtn.setOnClickListener(this);
    }

    void registerEditTextListener() {
        zhEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                zhTextCount = count;
                loginBtnState();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        mmEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mmTextCount = count;
                loginBtnState();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    void initLoginBtn() {
        loginBtnEnabled(false);
    }

    void loginBtnState() {
        if(zhTextCount>0 && mmTextCount>0) {//可点击状态
            loginBtnEnabled(true);
        } else {
            loginBtnEnabled(false);
        }
    }

    void loginBtnEnabled(boolean enable) {
        loginBtn.setEnabled(enable);
    }

    void login() {
        showToast(this, "登陆成功");
    }

    void toRegister() {
        showToast(this, "注册去");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icz_login_btn:
                login();
                break;
            case R.id.icz_login_to_zc:
                toRegister();
                break;
        }
    }
}
