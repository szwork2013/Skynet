package com.okar.icz.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.okar.icz.common.Constants;
import com.okar.icz.common.PageLoader;
import com.okar.icz.entry.Account;
import com.okar.icz.entry.Feed;

import java.util.List;

/**
 * Created by wangfengchen on 15/11/26.
 */
public class TestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_test);
        Log.d("test", "test");
    }
}
