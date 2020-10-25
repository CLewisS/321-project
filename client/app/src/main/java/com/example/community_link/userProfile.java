package com.example.community_link;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

public class userProfile {
    public String id;
    public String deviceToken;
    public userProfile(String ID){
        id = ID;
        deviceToken = "unInitialized";
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
