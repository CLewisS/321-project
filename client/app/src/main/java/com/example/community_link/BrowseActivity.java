package com.example.community_link;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.OptionalDouble;

public class BrowseActivity extends CommunityLinkActivity {

    // Constants
    private static final int LAT_MIN = 0;
    private static final int LAT_MAX = 1;
    private static final int LONG_MIN = 2;
    private static final int LONG_MAX = 3;
    private static final int REQUEST_CODE = 101;

    //private userProfile user;
    private List<ServiceData> sdList = new ArrayList<ServiceData>();
    private List<ServiceData> usedList = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLoc;
    private PopupWindow filtersPopup;
    private View filterLayout;



    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        LayoutInflater inflater = LayoutInflater.from(this);
        filterLayout = inflater.inflate(R.layout.filters_layout, null);
        filtersPopup = new PopupWindow(filterLayout);

        initFilters();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (filtersPopup.isShowing()) {
            filtersPopup.dismiss();
        }
    }

    /* Initialize the spinners (dropdown menus) in the filters popup menu
     *   - There are two spinners: distances and date range
     */
    private void initFilters() {
        // Distance spinner
        Spinner distFilters = (Spinner) filterLayout.findViewById(R.id.distanceFilter);
        ArrayAdapter<CharSequence> distAdapter = ArrayAdapter.createFromResource(this,
                R.array.dists_array, android.R.layout.simple_spinner_item);
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distFilters.setAdapter(distAdapter);

        // Date range spinner
        Spinner dateFilters = (Spinner) filterLayout.findViewById(R.id.dateFilter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dates_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateFilters.setAdapter(adapter);
    }

    /* Button onclick functions */

    public void search(View view) {
        sdList.clear();

        getLocation();

        if (filtersPopup.isShowing()) {
            filtersPopup.dismiss();
        }

        JSONObject conditions = getSearchConditionJSON();
        getServicesBrowse(conditions);
    }

    public void filters(View view) {
        if (filtersPopup.isShowing()) {
            filtersPopup.dismiss();
        } else {
            filtersPopup.showAsDropDown(view);
            filtersPopup.update(view.getWidth(), 700);
            filtersPopup.setTouchable(true);
            filtersPopup.setFocusable(true);
        }
    }

    public void viewOnMap(View view) {
        int index = (Integer) view.getTag();
        Intent mapActivity = new Intent(this, MapActivity.class);
        mapActivity.putExtra("service", sdList.get(index));
        startActivity(mapActivity);
    }

    public void getThisService(View view) {
        if (CommunityLinkApp.userLoggedIn()) {
            int index = (Integer) view.getTag();
            ServiceData sd = sdList.get(index);
            int serviceID = sd.getId();
            Log.w("get this service", String.valueOf(serviceID));

            Response.Listener useServiceCallback = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.print("Done");
                }
            };

            Response.ErrorListener useServiceErrorCallback = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.print("Died");
                }
            };

            CommunityLinkApp.requestManager.useService(CommunityLinkApp.user.getUsername(), serviceID, useServiceCallback, useServiceErrorCallback);
        } else {
            CharSequence toastMess = "Sorry, you must be logged in to RSVP.";
            Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getSuggestions(View view) {
        if(CommunityLinkApp.userLoggedIn()) {
            getUsedServices();
        } else {
            CharSequence toastMess = "You are not logged in.";
            Toast toast = Toast.makeText(view.getContext(), toastMess, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getUsedServices(){
        usedList.clear();
        sdList.clear();

        Response.Listener usedServiceResponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for (int index = 0; index < response.length(); index++) {
                    try {
                        usedList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (usedList.size() == 0){
                    CharSequence toastMess = "Sorry, in order to get suggestions you need to use more services.";
                    Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    JSONObject conditions = getSuggestedConditions();
                    getServicesSuggest(conditions);
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

        CommunityLinkApp.requestManager.getUserUsedService(CommunityLinkApp.user.getUsername(),
                usedServiceResponse, errorCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private JSONObject getSuggestedConditions() {
        List<Double> latList = new ArrayList<>();
        List<Double> lonList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        List<Integer> hourList = new ArrayList<>();
        List<Integer> minList = new ArrayList<>();

        for (ServiceData sd : usedList) {
            latList.add(sd.getLat());
            lonList.add(sd.getLongi());
            timeList.add(sd.getTime());
        }
        OptionalDouble lat = latList.stream().mapToDouble(x -> x).average();
        OptionalDouble lon = lonList.stream().mapToDouble(x -> x).average();
        double averageLat = lat.isPresent() ? lat.getAsDouble() : 0;
        double averageLon = lon.isPresent() ? lon.getAsDouble() : 0;
        int avgDist = 15;

        double[] coords = getCoords(avgDist, averageLat, averageLon);

        for(String time:timeList){
            hourList.add(Integer.parseInt(time.split(":")[0]));
            minList.add(Integer.parseInt(time.split(":")[1]));
        }
        OptionalDouble ho = hourList.stream().mapToDouble(x -> x).average();
        OptionalDouble mi = minList.stream().mapToDouble(x -> x).average();

        int avgHour = ho.isPresent() ? (int) Math.round(ho.getAsDouble()) : 0;
        int avgMin = mi.isPresent() ? (int) Math.round(mi.getAsDouble()) : 0;
        int timeDiff = 12;                                                   //min search time difference;

        int minHour = Math.max(avgHour - timeDiff / 2, 0);
        int maxHour = Math.min(avgHour + timeDiff / 2, 24);

        String timeMin = minHour + ":" + avgMin;
        String timeMax = maxHour + ":" + avgMin;

        JSONObject conditions = new JSONObject();
        try {
            if (coords.length == 4) {
                conditions.put("lat-min", coords[LAT_MIN]);
                conditions.put("lat-max", coords[LAT_MAX]);
                conditions.put("longi-min", coords[LONG_MIN]);
                conditions.put("longi-max", coords[LONG_MAX]);
            }

            String currDate = getCurrDate();
            conditions.put("date-min", currDate);
            conditions.put("time-min",timeMin);
            conditions.put("time-max", timeMax);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conditions;
    }

    /* HTTP request functions */

    private void getServicesBrowse(JSONObject conditions) {
        Response.Listener getServicesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for(int index=0; index<response.length(); index++) {
                    try {
                        sdList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (sdList.size() == 0) {
                    System.out.println("No services");
                    CharSequence errorMess = "Sorry, no services found. Please enter different search criteria";
                    Toast errorToast = Toast.makeText(getApplicationContext(), errorMess, Toast.LENGTH_LONG);
                    errorToast.setGravity(Gravity.CENTER, 0, 0);
                    errorToast.show();

                    LinearLayout serviceResults = findViewById(R.id.serviceResults);
                    serviceResults.removeAllViews();

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
            }
        };

        CommunityLinkApp.requestManager.getServices(conditions, getServicesResponseCallback, errorCallback);
    }

    private JSONObject getSearchConditionJSON() {
        EditText serviceSearch = findViewById(R.id.serviceSearch);
        String title = serviceSearch.getText().toString();

        EditText timeMinET = filterLayout.findViewById(R.id.timeMin);
        String timeMin = timeMinET.getText().toString();

        EditText timeMaxET = filterLayout.findViewById(R.id.timeMax);
        String timeMax = timeMaxET.getText().toString();

        String date = getSpinnerDate();


        JSONObject conditions = new JSONObject();
        try {

            if (title != null && !title.isEmpty()) {
                conditions.put("name", title);
            }

            double[] coords = getSpinnerDist();
            if (coords.length == 4) {
                conditions.put("lat-min", coords[LAT_MIN]);
                conditions.put("lat-max", coords[LAT_MAX]);
                conditions.put("longi-min", coords[LONG_MIN]);
                conditions.put("longi-max", coords[LONG_MAX]);
            }

            String currDate = getCurrDate();
            conditions.put("date-min", currDate);

            if (!date.isEmpty()) {
                conditions.put("date-max", date);
            }

            if (!timeMin.isEmpty()) {
                conditions.put("time-min", timeMin);
            }

            if (!timeMax.isEmpty()) {
                conditions.put("time-max", timeMax);
            }

        }catch(JSONException e) {
            e.printStackTrace();
        }

        return conditions;

    }

    private String getCurrDate() {
        DateFormat dateForm = new SimpleDateFormat("YYYY-MM-dd");
        return dateForm.format(Calendar.getInstance().getTime());

    }

    private double[] getSpinnerDist() {
        if (userLoc != null) {
            Spinner distFilter = filterLayout.findViewById(R.id.distanceFilter);
            String dist = distFilter.getSelectedItem().toString();

            double currLat = userLoc.getLatitude();
            double currLong = userLoc.getLongitude();

            if ("5 km".equals(dist)) {
                return getCoords(5, currLat, currLong);
            } else if ("10 km".equals(dist)) {
                return getCoords(10, currLat, currLong);
            } else if ("15 km".equals(dist)) {
                return getCoords(15, currLat, currLong);
            } else if ("25 km".equals(dist)) {
                return getCoords(25, currLat, currLong);
            } else if ("50 km".equals(dist)) {
                return getCoords(50, currLat, currLong);
            }
        }

        return new double[0];
    }

    private double[] getCoords(int dist, double currLat, double currLong) {
        double[] ret = new double[4];

        double latDiff = dist / 111.0;

        System.out.println("LatDiff " + latDiff);

        if(currLat >= 0){
            ret[LAT_MIN] = Math.max(currLat - latDiff, 0);
            ret[LAT_MAX] = Math.min(currLat + latDiff, 90);
        }
        else{
            ret[LAT_MAX] = Math.min(currLat + latDiff, 0);
            ret[LAT_MIN] = Math.max(currLat - latDiff, -90);
        }

        double longDiff = dist / (Math.cos(Math.toRadians(currLat)) * 111.0);

        if(currLong >= 0){
            ret[LONG_MIN] = Math.max(currLong - longDiff, 0);
            ret[LONG_MAX] = Math.min(currLong + longDiff, 180);
        }
        else{
            ret[LONG_MAX] = Math.min(currLong + longDiff, 0);
            ret[LONG_MIN] = Math.max(currLong - longDiff, -180);
        }


        return ret;
    }

    private String getSpinnerDate() {
        Spinner dateFilter = filterLayout.findViewById(R.id.dateFilter);
        String date = dateFilter.getSelectedItem().toString();
        DateFormat dateForm = new SimpleDateFormat("YYYY-MM-dd");
        Calendar calendar = Calendar.getInstance();
        System.out.println(date);

        if ("Today".equals(date)) {
            return dateForm.format(calendar.getTime());
        } else if ("This week".equals(date)) {
            calendar.add(Calendar.DATE, 7);
            return dateForm.format(calendar.getTime());
        } else if ("This month".equals(date)) {
            calendar.add(Calendar.DATE, 30);
            return dateForm.format(calendar.getTime());
        } else if ("This year".equals(date)) {
            calendar.add(Calendar.DATE, 365);
            return dateForm.format(calendar.getTime());
        }

        return "";
    }

    private void getServicesSuggest(JSONObject conditions) {
        Response.Listener getServicesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for(int index=0; index<response.length(); index++) {
                    try {
                        sdList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (sdList.size() == 0) {
                    System.out.println("No services");
                    CharSequence errorMess = "Sorry, no suggested services found.";
                    Toast errorToast = Toast.makeText(getApplicationContext(), errorMess, Toast.LENGTH_LONG);
                    errorToast.setGravity(Gravity.CENTER, 0, 0);
                    errorToast.show();

                    LinearLayout serviceResults = findViewById(R.id.serviceResults);
                    serviceResults.removeAllViews();

                } else {
                    CharSequence message = "Based on your history, we suggest these services.";
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    displayServices();
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

    /* View manipulation functions */

    private View getServiceResultView(int i) {
        ServiceData sd = sdList.get(i);
        LayoutInflater inflater = LayoutInflater.from(BrowseActivity.this);
        View serviceView = inflater.inflate(R.layout.service_result, null);

        TextView serviceTitle = serviceView.findViewById(R.id.serviceResultTitle);
        serviceTitle.setText(sd.getName());

        TextView owner = serviceView.findViewById(R.id.ownerResult);
        owner.setText("Provided by: " + sd.getOwner());

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

        TextView locationResult = serviceView.findViewById(R.id.locationResult);
        locationResult.setText("Location: (" + sd.getLat() + ", " +sd.getLongi() + ")");

        TextView descriptionResult = serviceView.findViewById(R.id.descriptionResult);
        descriptionResult.setText(sd.getDescription());

        Button mapButt = serviceView.findViewById(R.id.mapButt2);
        mapButt.setTag(i);
        Button getButt = serviceView.findViewById(R.id.getThisService);
        getButt.setTag(i);

        return serviceView;
    }

    private void displayServices() {
        LinearLayout serviceResults = findViewById(R.id.serviceResults);
        serviceResults.removeAllViews();
        for (int i = 0; i < sdList.size(); i++) {
            View resultView = getServiceResultView(i);
            serviceResults.addView(resultView);
        }
    }

    //This isn't working for some reason

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            System.out.println("Couldn't get permission");

        } else {
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        System.out.println("GOT LOCATION " + location);
                        userLoc = location;
                    }
                }
            });
        }
    }


}