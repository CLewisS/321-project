package com.example.community_link;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class userProfile {
    public String id;
    public String deviceToken;
    public userProfile(String ID){
        id = ID;
        deviceToken = "unInitialized";
    }
}
