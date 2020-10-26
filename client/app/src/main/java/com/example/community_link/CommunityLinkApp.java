package com.example.community_link;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
