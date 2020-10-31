package com.example.community_link;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

public class UserProfile {
    public String username;
    private String password;
    public String deviceToken;
    public UserProfile(String username, String password){
        this.username = username;
        this.password = password;
        this.deviceToken = "";
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public boolean equals(UserProfile other) {
        return this.username.equals(other.getUsername()) && this.password.equals(other.getPassword());
    }

    /** The following part is the prototype of Suggestion Service
     *  Some Ideas:
     *  The Location: Current Location
     *  The Distance: 5km?
     *  The type: Usually access type
     *  Date and Time: In the next week
     *  <p></>
     * @param void
     * @return  The JSONObject of suggested service based on position, type and time range
     */
    /*
    public JSONObject getSuggestedService(){
        JSONObject suggestion = new JSONObject();
        try {

        }catch(JSONException e) {
            e.printStackTrace();
        }
        return suggestion;
    }
    */
}
