package com.i61.parent;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by linxiaodong on 2018/3/14.
 */

public class parentApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
