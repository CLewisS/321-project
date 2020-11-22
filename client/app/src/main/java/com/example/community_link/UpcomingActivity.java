package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class UpcomingActivity extends CommunityLinkActivity {

    private ArrayList<ServiceData> sdList = new ArrayList<ServiceData>();
    private AdapterView.OnItemSelectedListener modeSpinnerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        modeSpinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getRsvpServices();
                } else if (position == 1) {
                    getOwnedServices();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        initSpinners();
    }

    private void initSpinners() {
        // Distance spinner
        Spinner upcomingMode = (Spinner) findViewById(R.id.upcomingMode);
        ArrayAdapter<CharSequence> upcomingAdapter = ArrayAdapter.createFromResource(this,
                R.array.upcoming_modes, android.R.layout.simple_spinner_item);
        upcomingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upcomingMode.setAdapter(upcomingAdapter);

        upcomingMode.setOnItemSelectedListener(modeSpinnerListener);

    }



    private void getRsvpServices() {
        sdList.clear();
        LinearLayout serviceResults = findViewById(R.id.rsvpResults);
        serviceResults.removeAllViews();

        Response.Listener usedServiceResponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for (int index = 0; index < response.length(); index++) {
                    try {
                        sdList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (sdList.size() == 0){
                    CharSequence toastMess = "You have no services that you have RSVP'd for.";
                    Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    displayServices();
                }
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());

                CharSequence toastMess = "Sorry, the services you RSVP'd for couldn't be found at the moment.";
                Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        };

        CommunityLinkApp.requestManager.getUserUsedService(CommunityLinkApp.user.getUsername(),
                usedServiceResponse, errorCallback);
    }

    private void getOwnedServices() {
        sdList.clear();
        LinearLayout serviceResults = findViewById(R.id.rsvpResults);
        serviceResults.removeAllViews();

        Response.Listener usedServiceResponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for (int index = 0; index < response.length(); index++) {
                    try {
                        sdList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (sdList.size() == 0){
                    CharSequence toastMess = "You have no services that you are providing.";
                    Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    displayServices();
                }
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());

                CharSequence toastMess = "Sorry, the services you are providing couldn't be found at the moment.";
                Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        };

        CommunityLinkApp.requestManager.getOwnedServices(CommunityLinkApp.user.getUsername(),
                usedServiceResponse, errorCallback);
    }


    /* Service scroll view manipulation functions
    *
    *  setResultViewDate - Helper function. It Sets the date string.
    *  setViewTags - Helper function. It sets the tags for the buttons,
    *                so that they match the service they belong to.
    *  getServiceResultView - Helper function. It constructs the view for a single service.
    *  displayServices - Adds all of the service views to the UI.
    **/

    private void setResultViewDate(ServiceData sd, View serviceView) {
        TextView dateResult = serviceView.findViewById(R.id.dateTimeResult);
        String date = sd.getDate().split("T")[0];
        String [] dateSplit = date.split("-");
        String dow = sd.getDow();
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(dateSplit[1])-1];
        String day = dateSplit[2];
        String year = dateSplit[0];
        String time = sd.getTime();
        String[] hourMin = time.split(":");
        dateResult.setText(dow + ", " + month + " " + day + ", " + year + " starts at " + hourMin[0] + ":" + hourMin[1]);
    }

    private void setViewTags(View serviceView, int i, String owner) {
        Button mapButt = serviceView.findViewById(R.id.mapButt2);
        mapButt.setTag(i);
        Button getButt = serviceView.findViewById(R.id.getThisService);
        getButt.setTag(i);
        Button messageButt = serviceView.findViewById(R.id.messageProvButt);
        if (CommunityLinkApp.userLoggedIn() && !CommunityLinkApp.user.getUsername().equals(owner)) {
            messageButt.setTag(i);
        } else {
            messageButt.setVisibility(View.GONE);
        }
    }

    private View getServiceResultView(int i) {
        ServiceData sd = sdList.get(i);
        LayoutInflater inflater = LayoutInflater.from(UpcomingActivity.this);
        View serviceView = inflater.inflate(R.layout.service_result, null);

        Button getButt = serviceView.findViewById(R.id.getThisService);
        getButt.setVisibility(View.GONE);

        TextView serviceTitle = serviceView.findViewById(R.id.serviceResultTitle);
        serviceTitle.setText(sd.getName());

        TextView owner = serviceView.findViewById(R.id.ownerResult);
        owner.setText("Provided by: " + sd.getOwner());

        setResultViewDate(sd, serviceView);

        TextView locationResult = serviceView.findViewById(R.id.locationResult);
        locationResult.setText("Location: (" + sd.getLat() + ", " +sd.getLongi() + ")");

        TextView descriptionResult = serviceView.findViewById(R.id.descriptionResult);
        descriptionResult.setText(sd.getDescription());

        setViewTags(serviceView, i, sd.getOwner());

        return serviceView;
    }

    private void displayServices() {
        LinearLayout serviceResults = findViewById(R.id.rsvpResults);
        serviceResults.removeAllViews();

        for (int i = 0; i < sdList.size(); i++) {
            View resultView = getServiceResultView(i);
            serviceResults.addView(resultView);
        }
    }
}