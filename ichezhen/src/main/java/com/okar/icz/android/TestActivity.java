package com.okar.icz.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.inject.Inject;
import com.okar.icz.common.BaseActivity;
import com.okar.icz.common.Constants;
import com.okar.icz.common.PageLoader;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Feed;
import com.okar.icz.view.PhotoView;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class TestActivity extends BaseActivity {

    @InjectView(R.id.view)
    PhotoView photoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.d("test", "test");
        photoView.setPhotoViewCallback(new PhotoView.PhotoViewCallback() {
            @Override
            public void onClickAddPhoto() {
                String fp = Constants.TEMP_ROOT_DIR+ "/a.jpg";
                Log.d("test", "fp "+fp);
                photoView.addPhoto(fp, "");
            }
        });
    }
}
