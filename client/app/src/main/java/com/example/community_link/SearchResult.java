package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SearchResult extends AppCompatActivity {
    //private userProfile user;
    private List<ServiceData> sdList = new ArrayList<ServiceData>();
    private int i = 0;
    private TextView txv;

    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        /*<--------------HARDCODING----------------------->*/
        ServiceData sd1 = new ServiceData();
        sd1.setDescription("An O(n) Sorting Algorithm, iterating the array and remove " +
                "the not-in-order elements");
        sd1.setType("Sorting");
        sd1.setDate(1938,6,4);
        sd1.setName("Joseph Vissarionovich Stalin");
        sd1.setEventName("Stalin Sort");
        sd1.setTime(0,0);

        ServiceData sd2 = new ServiceData();
        sd2.setDescription("An O(1) Sorting Algorithm, simply claimint the array is already" +
                "sorted, and those who against are CNN and CHINESE media");
        sd2.setType("Sorting");
        sd2.setDate(2017,1,20);
        sd2.setName("Donald John Trump");
        sd2.setEventName("Trump Sort");
        sd2.setTime(8,41);

        sdList.add(sd1);
        sdList.add(sd2);
        /*<--------------HARDCODING----------------------->*/

        /*<--Get Suggestion Feature-->*/
        /*
        JsonObject suggestion = user.getSuggestion();
         Response.Listener getServicesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //add to ArrayList
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.getServices(suggestion, getServicesResponseCallback, errorCallback);
         */

        txv = (TextView) findViewById(R.id.owner);
        txv.setText(sdList.get(i).toString());
    }

    public void goPrev(View view){

        if(i!=0){
            i--;
        }
        txv.setText(sdList.get(i).toString());

    }

    public void goNext(View view){
        if(i != 1) {
            i++;
        }
        txv.setText(sdList.get(i).toString());
    }


}