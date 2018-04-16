package com.nfsapp.surbhi.nfsapplication.other;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by surbhi on 3/28/2018.
 */

public class NFSApp extends MultiDexApplication {
    private static NFSApp instance;

    public static NFSApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/estre.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/estre.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/estre.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/estre.ttf");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}