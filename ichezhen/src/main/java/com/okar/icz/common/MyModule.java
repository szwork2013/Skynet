package com.okar.icz.common;

import android.content.Context;

import com.google.inject.AbstractModule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangfengchen on 15/10/30.
 */
public class MyModule extends AbstractModule {

    private Context context;//系统会自己传入上下文

    public MyModule(Context context) {
        this.context = context;
    }

    @Override
    protected void configure() {
        bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
        bind(SystemSettings.class).toInstance(SystemSettings.getSystemSettings(context));
    }
}
