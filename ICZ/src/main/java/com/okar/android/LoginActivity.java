package com.okar.android;

import android.widget.EditText;

import com.works.skynet.base.BaseActivity;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/1/15.
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.login_edit_username)
    EditText usernameEt;

    @InjectView(R.id.login_edit_password)
    EditText passwordEt;

    @InjectView(R.id.login_submit)
    EditText submitBtn;

    @Override
    protected void init() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onListener() {

    }
}
