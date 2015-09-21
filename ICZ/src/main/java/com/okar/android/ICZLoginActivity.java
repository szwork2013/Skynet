package com.okar.chatservice;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.j256.ormlite.logger.LoggerFactory;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.okar.utils.Constants;
import com.okar.utils.URI;
import com.works.skynet.base.BaseActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/3/16.
 */
public class ICZLoginActivity extends BaseActivity{

    private final com.j256.ormlite.logger.Logger log = LoggerFactory.getLogger(ICZWebActivity.class);

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
        String username = zhEt.getText().toString();
        String password = mmEt.getText().toString();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        client.post(URI.LOGIN, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                if(response!=null) {
                    log.info("login result "+response);
                    String type = response.optString(Constants.TYPE);
                    if(Constants.SUCCESS.equals(type)) {
                        showToast("登陆成功");

                    }else {
                        showToast("账号或密码错误，请重试！");
                    }
                }
            }
        });
    }

    void toRegister() {
        showToast("注册去");
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
