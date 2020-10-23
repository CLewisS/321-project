package com.example.community_link;

import android.app.Application;
import android.media.MediaDrm;
import android.net.Network;
import android.util.Log;

import androidx.constraintlayout.solver.Cache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class RequestManager {

    private DiskBasedCache cache;

    private BasicNetwork network;

    private RequestQueue requestQueue;

    private static final String serverUrl = "http://ec2-3-13-46-252.us-east-2.compute.amazonaws.com:8080"; // MUST BE SET TO SERVER URL

    private static final ArrayList<String> validServiceConditions = new ArrayList<>(Arrays.asList("date-min", "date-max",
            "time-min", "time-max",
            "lat-min", "lat-max",
            "longi-min", "longi-max",
            "type"));;

    public RequestManager(DiskBasedCache cache) {

        this.cache = cache;
        // Set up the network to use HttpURLConnection as the HTTP client.
        this.network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        this.requestQueue = new RequestQueue(cache, network);

        this.requestQueue.start();
    }

    /**
     *  Gets services that meet the specified conditions.
     *
     * @param conditions A JSON object containing the conditions for services.
     *                   Conditions must be for maximums or minimums (except for type) of valid service attributes.
     *
     *                   Valid service attributes:
     *                     - date (Format: "YYYY-MM-DD")
     *                     - time (Format: "hh:mm:ss")
     *                     - lat (latitudinal coordinate)
     *                     - longi (longitudinal coordinate)
     *                     - type (category of the service)
     *
     *                   Example: conditions = {date-min: "2020-10-15", lat-min: 48.61284, lat-max: 56.73}
     *
     * @param getServicesCallback A callback function for a response. Example in RequestExample activity.
     * @param getServicesErrorCallback A callback function for an error
     */
    public void getServices(JSONObject conditions, Response.Listener getServicesCallback, Response.ErrorListener getServicesErrorCallback) {
        StringBuilder endpoint = new StringBuilder();
        endpoint.append("/service?");

        Iterator<String> keys = conditions.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (validServiceConditions.contains(key)) {
                try {
                    endpoint.append(key + "=" + conditions.get(key) + "&");
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        endpoint.deleteCharAt(endpoint.length() - 1);
        System.out.println(endpoint);
        sendGetRequest(endpoint.toString(), getServicesCallback, getServicesErrorCallback);
    }

    /**
     *  Add a new service.
     *
     * @param service A JSON object containing the service attributes.
     *                Valid service attributes:
     *                  - name (The title of the service)
     *                  - date (Format: "YYYY-MM-DD")
     *                  - dow (day of the week)
     *                  - time (Format: "hh:mm:ss")
     *                  - lat (latitudinal coordinate)
     *                  - longi (longitudinal coordinate)
     *                  - owner (The name of the person who created the service)
     *                  - type (category of the service)
     *                  - description (A description of the service provided)
     *
     * @param addServiceCallback A callback function for a response. Example in RequestExample activity.
     * @param addServiceErrorCallback A callback function for an error
     */
    public void addService(JSONObject service, Response.Listener addServiceCallback, Response.ErrorListener addServiceErrorCallback) {
        sendPostRequest("/service", service, addServiceCallback, addServiceErrorCallback);
    }

    private void sendGetRequest(String endpoint, Response.Listener responseCallback, Response.ErrorListener errorCallback) {

        // Formulate the request and handle the response.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, serverUrl + endpoint, null,
                responseCallback,
                errorCallback);

        requestQueue.add(request);

    }

    private void sendPostRequest(String endpoint, JSONObject body, Response.Listener responseCallback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverUrl + endpoint, body,
                responseCallback,
                errorCallback) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        requestQueue.add(request);

    }
}
