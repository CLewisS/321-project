package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class RequestExample extends AppCompatActivity {

    private TextView res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_example);

        res = findViewById(R.id.res);
    }

    public void getServicesButton(View view) {
        JSONObject conditions = new JSONObject();

        Response.Listener getServicesCallback = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                res.setText(response);
            }
        };

        Response.ErrorListener getServicesErrorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };
        CommunityLinkApp.requestManager.getServices(conditions, getServicesCallback, getServicesErrorCallback);
    }
}