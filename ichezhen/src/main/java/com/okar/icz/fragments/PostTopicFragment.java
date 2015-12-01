package com.okar.icz.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.okar.icz.android.R;
import com.okar.icz.common.ProgressView;
import com.okar.icz.common.ShowTextLengthTextWatcher;
import com.okar.icz.common.SystemSettings;
import com.okar.icz.utils.HttpClient;
import com.okar.icz.utils.StringUtils;
import com.okar.icz.view.PhotoView;
import com.okar.icz.view.photo.PickImageBaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PostTopicFragment extends PickImageBaseFragment {

    public int topicType;


    @InjectView(R.id.photo_view)
    PhotoView photoView;

    @InjectView(R.id.post_topic)
    Button postTopicBtn;

    @InjectView(R.id.input)
    EditText inputET;

    @InjectView(R.id.post_topic_text_count)
    TextView showTextLenTV;

    @Inject
    SystemSettings settings;

    ProgressView progressView;

    public static PostTopicFragment getInstance(int topicType) {
        PostTopicFragment fragment = new PostTopicFragment();
        Bundle args = new Bundle();
        args.putInt("topicType", topicType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicType = getArguments().getInt("topicType");
        settings.getAccountId();
    }

    @Override
    protected View getRootView() {
        return inflater.inflate(R.layout.fragment_post_topic,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoView.setPhotoViewCallback(new PhotoView.PhotoViewCallback() {
            @Override
            public void onClickAddPhoto() {
                pickImage(false);
            }
        });
        postTopicBtn.setOnClickListener(this);
        progressView = new ProgressView(getActivity(), (ViewGroup) view);
        initInput();
    }

    void initInput() {
        showTextLenTV.setText("0/400");
        inputET.addTextChangedListener(new ShowTextLengthTextWatcher(400, inputET, showTextLenTV));
        //设置EditText的显示方式为多行文本输入
        inputET.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //改变默认的单行模式
        inputET.setSingleLine(false);
        //水平滚动设置为False
        inputET.setHorizontallyScrolling(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_topic:
                postTopic();
                break;
        }
    }

    @Override
    protected void pickImageResult(Bitmap bitmap, final String filePath) {
        Log.d("result", filePath);
        progressView.show();
        HttpClient.getInstance().uploadImage(filePath, new JsonHttpResponseHandler() {

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
                Log.d("upload", "response " + response);
                photoView.addPhoto("file:/" + filePath, response.optString("url"));
            }
        });
    }

    void postTopic() {
        String text = inputET.getText().toString();
        if (StringUtils.isBlank(text)) {
            //不能为空
            showToast("帖子内容不能为空");
            return;
        }
        postTopicBtn.setEnabled(false);
        progressView.show();
        List<String> photos = photoView.getPhotoUrls();
        try {
            HttpClient.getInstance().postTopicOrQuestion(getActivity(), settings.getAccountId(), settings.getUid(), text, photos, topicType, new JsonHttpResponseHandler() {

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    progressView.progress(bytesWritten, totalSize);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    postTopicBtn.setEnabled(true);
                    progressView.dismiss();
                }

                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    Log.d("postTopicd", "response " + response);
                    showToast(response.optString("message"));
                    if (HttpClient.isSuccess(response)) {
                        //成功跳转
                    }
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
