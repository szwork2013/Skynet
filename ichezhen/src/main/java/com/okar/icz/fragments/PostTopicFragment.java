package com.okar.icz.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.okar.icz.android.R;
import com.okar.icz.common.BaseFragment;
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

    public static final int TOPIC = 31;
    public static final int QUESTION = 38;

    public int topicType;


    @InjectView(R.id.photo_view)
    PhotoView photoView;

    @InjectView(R.id.post_topic)
    Button postTopicBtn;

    @InjectView(R.id.input)
    EditText inputET;

    @Inject
    SystemSettings settings;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_topic, container, false);
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
        HttpClient.getInstance().uploadImage(filePath, new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
                super.onFinish();

            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.d("upload", "response " + response);
                photoView.addPhoto("file:/"+filePath, response.optString("url"));
            }
        });
    }

    void postTopic() {
        String text = inputET.getText().toString();
        if(StringUtils.isBlank(text)) {
            //不能为空
            showToast("帖子内容不能为空");
            return;
        }
        List<String> photos = photoView.getPhotoUrls();
        try {
            HttpClient.getInstance().postTopicOrQuestion(getActivity(), settings.getAccountId(), settings.getUid(), text, photos, topicType, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    Log.d("postTopicd", "response " + response);
                    showToast(response.optString("message"));
                    if(HttpClient.isSuccess(response)) {
                        //成功跳转
                    }
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
