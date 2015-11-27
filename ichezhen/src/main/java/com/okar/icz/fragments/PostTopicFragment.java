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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.okar.icz.android.R;
import com.okar.icz.common.BaseFragment;
import com.okar.icz.utils.HttpClient;
import com.okar.icz.view.PhotoView;
import com.okar.icz.view.photo.PickImageBaseFragment;

import org.json.JSONObject;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/11/27.
 */
public class PostTopicFragment extends PickImageBaseFragment {

    @InjectView(R.id.photo_view)
    PhotoView photoView;

    @InjectView(R.id.post_topic)
    Button postTopicBtn;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_topic:
                for (String s: photoView.getPhotoUrls()) {
                    Log.d("s", s);
                }
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
}
