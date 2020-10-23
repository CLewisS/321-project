package com.example.community_link;

import android.app.Application;

import com.android.volley.toolbox.DiskBasedCache;

public class CommunityLinkApp extends Application {
    public static RequestManager requestManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DiskBasedCache requestCache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        this.requestManager = new RequestManager(requestCache);
    }
}
