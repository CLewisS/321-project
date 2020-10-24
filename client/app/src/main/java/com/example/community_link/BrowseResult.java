package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowseResult extends AppCompatActivity {

    private List<ServiceData> sdList = new ArrayList<ServiceData>();
    private int i = 0;
    private TextView txv;
    private int size;


    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_result);

        /*<--------------HARDCODING----------------------->*/
        txv = (TextView) findViewById(R.id.browseResult);

        JSONObject conditions = new JSONObject();
        try {
            conditions.put("date-min", "2020-10-15");
            conditions.put("lat-min", 48.61284);
            conditions.put("lat-max", 56.73);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        Response.Listener getServicesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for(int index=0;index<response.length();index++) {
                    try {
                        sdList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                size = sdList.size();
                txv.setText(sdList.get(i).toString());
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.getServices(conditions, getServicesResponseCallback, errorCallback);

        /*<--------------HARDCODING----------------------->*/

    }

    public void goPrev(View view){
        if(i != 0){
            i--;
        }
        txv.setText(sdList.get(i).toString());

    }

    public void goNext(View view){
        if(i != size-1) {
            i++;
        }
        txv.setText(sdList.get(i).toString());
    }
}