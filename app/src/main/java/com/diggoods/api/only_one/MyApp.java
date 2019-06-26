package com.diggoods.api.only_one;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Create by  FengJianyi on 2019/6/24
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
