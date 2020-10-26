package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowseResult extends CommunityLinkActivity {

    //private userProfile user;
    private List<ServiceData> sdList = new ArrayList<ServiceData>();
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE = 101;
    private int i = 0;
    private TextView txv;
    private int size;
    private Location userLoc;
    private final float NO_VAL = 6379;


    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_result);

        /*<--------------HARDCODING----------------------->*/
        //txv = (TextView) findViewById(R.id.browseResult);

        // Location isn't working yet
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //getLocation();

        // Retrieve search criteria for services
        Bundle searchCriteria = getIntent().getExtras();

        if (searchCriteria.getBoolean("suggestions")) {
            getSuggestions();
        } else {
            JSONObject conditions = getSearchConditionJSON(searchCriteria);
            System.out.println(conditions);

            getServices(conditions);
        }

    }

    private void getSuggestions() {
        System.out.println("Getting suggestions");
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
    }

    private void getServices(JSONObject conditions) {
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
                if (size == 0) {
                    System.out.println("No services");
                    CharSequence errorMess = "Sorry, no services found. Please enter different search criteria";
                    Toast errorToast = Toast.makeText(getApplicationContext(), errorMess, Toast.LENGTH_LONG);
                    errorToast.setGravity(Gravity.CENTER, 0, 0);
                    errorToast.show();

                    Intent browseService = new Intent(getApplicationContext(), BrowseServiceCond.class);
                    startActivity(browseService);
                } else {
                    LinearLayout serviceResults = findViewById(R.id.serviceResults);
                    for (int i = 0; i < size; i++) {
                        View resultView = getServiceResultView(sdList.get(i));
                        serviceResults.addView(resultView);
                    }
                }
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
    }

    private JSONObject getSearchConditionJSON(Bundle searchCriteria) {

        float currLat = searchCriteria.getFloat("currLat", NO_VAL);
        float currLong = searchCriteria.getFloat("currLong", NO_VAL);
        float dist = searchCriteria.getFloat("dist", NO_VAL);

        String dateMin = searchCriteria.getString("date-min");
        String dateMax = searchCriteria.getString("date-max");
        System.out.println(dateMax + dateMin);

        String timeMin = searchCriteria.getString("time-min");
        String timeMax = searchCriteria.getString("time-max");
        System.out.println(timeMin + " " + timeMax);

        String title = getIntent().getExtras().getString("title");
        System.out.println("Title: " + title);

        JSONObject conditions = new JSONObject();
        try {
            conditions.put("date-min", dateMin);
            conditions.put("date-max", dateMax);

            if (timeMin != null && !timeMin.isEmpty()) {
                conditions.put("time-min", timeMin);
            }
            if (timeMax != null && !timeMax.isEmpty()) {
                conditions.put("time-max", timeMax);
            }
            if (title != null && !title.isEmpty()) {
                conditions.put("name", title);
            }
            if (currLat != NO_VAL && dist != NO_VAL) {
                float latDiff = dist / 111;
                float latMin = currLat - latDiff;
                float latMax = currLat + latDiff;
                conditions.put("lat-min", latMin);
                conditions.put("lat-max", latMax);
            }
            if (currLong != NO_VAL && dist != NO_VAL) {
                float longDiff =  dist / (float)(Math.cos(Math.toRadians(currLat)) * 111);
                float longMin = currLong - longDiff;
                float longMax = currLong + longDiff;
                conditions.put("longi-min", longMin);
                conditions.put("longi-max", longMax);
            }

        }catch(JSONException e) {
            e.printStackTrace();
        }

        return conditions;

    }

    private View getServiceResultView(ServiceData sd) {
        LayoutInflater inflater = LayoutInflater.from(BrowseResult.this);
        View serviceView = inflater.inflate(R.layout.service_result, null);

        System.out.println(sdList.get(i));
        TextView serviceTitle = serviceView.findViewById(R.id.serviceResultTitle);
        serviceTitle.setText(sd.getName());

        TextView owner = serviceView.findViewById(R.id.ownerResult);
        owner.setText("Provided by: " + sd.getOwner());

        TextView dateResult = serviceView.findViewById(R.id.dateTimeResult);
        String date = sd.getDate().split("T")[0];
        String [] dateSplit = date.split("-");
        String dow = sd.getDow();
        String month = dateSplit[0];
        String day = dateSplit[1];
        String year = dateSplit[2];
        String time = sd.getTime();
        dateResult.setText(dow + ", " + month + " " + day + ", " + year + " starts at " + time);

        TextView locationResult = serviceView.findViewById(R.id.locationResult);
        locationResult.setText("Location: (" + sd.getLat() + ", " +sd.getLongi() + ")");

        TextView descriptionResult = serviceView.findViewById(R.id.descriptionResult);
        descriptionResult.setText(sd.getDescription());

        return serviceView;
    }

    public void goPrev(View view){
        if(i != 0){
            i--;
            //showService(sdList.get(i));
        } else {
            CharSequence message = "You are at the beginning of the services found";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


    }

    public void goNext(View view){
        if(i != size-1) {
            i++;
            //showService(sdList.get(i));
        } else {
            CharSequence message = "Sorry, no more services meet the search criteria";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    //This isn't working for some reason
/*
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            System.out.println("Couldn't get permission _____________-------------------__________________---------");
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    System.out.println("GOT LOCATION _____________-------------------__________________---------");
                    userLoc = location;
                    Toast.makeText(getApplicationContext(), userLoc.getLatitude() + "" + userLoc.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public void viewOnMap(View view) {
        Intent mapActivity = new Intent(this, MapActivity.class);
        mapActivity.putExtra("service", sdList.get(i));
        startActivity(mapActivity);
    }
}