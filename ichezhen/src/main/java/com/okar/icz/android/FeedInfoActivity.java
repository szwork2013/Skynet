package com.okar.icz.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.okar.icz.common.BaseActivity;
import com.okar.icz.common.Constants;
import com.okar.icz.common.ProgressView;
import com.okar.icz.common.SystemSettings;
import com.okar.icz.entry.Feed;
import com.okar.icz.fragments.FeedInfoFragment;
import com.okar.icz.utils.DensityUtils;
import com.okar.icz.utils.HttpClient;
import com.okar.icz.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/12/1.
 */
public class FeedInfoActivity extends BaseActivity {

    @InjectView(R.id.comment_btn)
    Button commentBtn;
    @InjectView(R.id.input_comment)
    EditText inputCommentET;
    @Inject
    SystemSettings settings;

    Feed feed;

    ProgressView progressView;

    FeedInfoFragment feedInfoFragment;

    final String TAG = "FeedInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_info);
        initView();
        feed = getIntent().getParcelableExtra("feed");
        feedInfoFragment = FeedInfoFragment.getInstance(feed);
        getSupportFragmentManager().beginTransaction().add(R.id.content, feedInfoFragment).commitAllowingStateLoss();
    }

    void initView() {
        View content = findViewById(R.id.content);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        int height = getScreenHeight();
        height = DensityUtils.px2dip(this, height);
        layoutParams.height = DensityUtils.dip2px(this, height - 50 - 40 - 25);
        commentBtn.setOnClickListener(this);
        progressView = new ProgressView(this, (ViewGroup) content.getParent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_btn:
                try {
                    postComment();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                    progressView.dismiss();
                }
                break;
        }
    }

    void postComment() throws UnsupportedEncodingException, JSONException {
        String text = inputCommentET.getText().toString();
        if(StringUtils.isBlank(text)) {
            showToast("请输入评论内容");
            return;
        }
        progressView.show();
        HttpClient.getInstance().commentFeed(this, settings.getAccountId(), settings.getUid(), null,
                feed.getId(), text, new JsonHttpResponseHandler() {

                    @Override
                    public void onProgress(int bytesWritten, int totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                        progressView.progress(bytesWritten, totalSize);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        progressView.dismiss();
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                        Log.d(TAG, "response " + response);
                        showToast(response.optString("message"));
                        if (HttpClient.isSuccess(response)) {
                            inputCommentET.setText("");
                            feedInfoFragment.onRefresh();
                        }
                    }
                });
    }
}
