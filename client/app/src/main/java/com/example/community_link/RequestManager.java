package com.example.community_link;

import android.app.Application;
import android.app.VoiceInteractor;
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

    //private static final String serverUrl = "http://ec2-3-13-46-252.us-east-2.compute.amazonaws.com:8080"; // MUST BE SET TO SERVER URL
    private static final String serverUrl = "http://ec2-3-13-46-252.us-east-2.compute.amazonaws.com:5150"; // MUST BE SET TO SERVER URL
   // private static final String serverUrl = "http://ec2-3-13-46-252.us-east-2.compute.amazonaws.com:8080"; // MUST BE SET TO SERVER URL
    //private static final String serverUrlNz = "http://nazokunvm.eastus.cloudapp.azure.com:8080";      //For Alex's use only

    private static final ArrayList<String> validServiceConditions = new ArrayList<>(Arrays.asList("date-min", "date-max",
            "time-min", "time-max",
            "lat-min", "lat-max",
            "longi-min", "longi-max",
            "type", "name", "dow", "owner"));

    private static final ArrayList<String> validmessageConditions = new ArrayList<>(Arrays.asList("user1", "user2", "newest", "timestamp"));

    public RequestManager(DiskBasedCache cache) {

        this.cache = cache;
        // Set up the network to use HttpURLConnection as the HTTP client.
        this.network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        this.requestQueue = new RequestQueue(cache, network);

        this.requestQueue.start();
    }

    private String conditionsToQueryString(JSONObject conditions, ArrayList validConditions) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("?");
        Iterator<String> keys = conditions.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (validConditions.contains(key)) {
                try {
                    queryString.append(key + "=" + conditions.get(key) + "&");
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        queryString.deleteCharAt(queryString.length() - 1);
        System.out.println("Query string: " + queryString);

        return queryString.toString();
    }

    public void useService(String username, int serviceID, Response.Listener useServiceCallback, Response.ErrorListener useServiceErrorCallback) {
        JSONObject userService = new JSONObject();
        try {
            userService.put("username", username);
            userService.put("serviceID", serviceID);
        }  catch (JSONException e) {
            e.printStackTrace();
        }

        sendPostRequest("/service/use", userService, useServiceCallback, useServiceErrorCallback);
    }

    public void getUserUsedService(String username, Response.Listener usedServiceCallBack, Response.ErrorListener usedServiceError){
        String endpoint = "/service/use?username=" + username + "&status=receive";
        sendGetRequest(endpoint , usedServiceCallBack, usedServiceError);
    }

    public void deleteUser(Response.Listener deleteUserCallback, Response.ErrorListener deleteUserErrorCallback) {
        sendDeleteRequest("/user?username=" + CommunityLinkApp.user.getUsername(), new JSONObject(), deleteUserCallback, deleteUserErrorCallback);
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
        endpoint.append("/service" + conditionsToQueryString(conditions, validServiceConditions));
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
        Log.w("ADD SERVICE", service.toString());
        sendPostRequest("/service", service, addServiceCallback, addServiceErrorCallback);
    }

    /**
     *  Gets messages that meet the specified conditions.
     *
     * @param conditions A JSON object containing the conditions for services.
     *                   Example: conditions = {user1: "Me", user2: "you", timestamp: "2020-10-15 12:30:33"}
     *
     * @param getMessagesCallback A callback function for a response. Example in RequestExample activity.
     * @param getMessagesErrorCallback A callback function for an error
     */
    public void getMessages(JSONObject conditions, Response.Listener getMessagesCallback, Response.ErrorListener getMessagesErrorCallback) {
        StringBuilder endpoint = new StringBuilder();
        endpoint.append("/chat" + conditionsToQueryString(conditions, validmessageConditions));
        System.out.println(endpoint);
        sendGetRequest(endpoint.toString(), getMessagesCallback, getMessagesErrorCallback);
    }

    /**
     *  Add a new message.
     *
     * @param message A JSON object containing the message attributes.
     *                Message Attributes: sender, recipient, timestamp, content
     *
     * @param addMessageCallback A callback function for a response. Example in RequestExample activity.
     * @param addMessageErrorCallback A callback function for an error
     */
    public void addMessage(JSONObject message, Response.Listener addMessageCallback, Response.ErrorListener addMessageErrorCallback) {
        sendPostRequest("/chat", message, addMessageCallback, addMessageErrorCallback);
    }

    /**
     *  Add a new user.
     *
     * @param user A JSON object containing the user attributes.
     *                User Attributes: username, password, FCM device token
     *
     * @param addUserCallback A callback function for a response.
     * @param addUserErrorCallback A callback function for an error
     */
    public void addUser(JSONObject user, Response.Listener addUserCallback, Response.ErrorListener addUserErrorCallback) {
        sendPostRequest("/user", user, addUserCallback, addUserErrorCallback);
    }

    public void authenticateUser(JSONObject user, Response.Listener authUserCallback, Response.ErrorListener authUserErrorCallback) {
        sendPutRequest("/user/login", user, authUserCallback, authUserErrorCallback);
    }

    public void updateUser(JSONObject user, Response.Listener updateUserCallback, Response.ErrorListener updateUserErrorCallback) {
        sendPutRequest("/user", user, updateUserCallback, updateUserErrorCallback);
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

    private void sendPutRequest(String endpoint, JSONObject body, Response.Listener responseCallback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, serverUrl + endpoint, body,
                responseCallback,
                errorCallback) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        requestQueue.add(request);

    }

    private void sendDeleteRequest(String endpoint, JSONObject body, Response.Listener responseCallback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, serverUrl + endpoint, body,
                responseCallback,
                errorCallback) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        requestQueue.add(request);

    }

/*
    private void sendPutRequest(String endpoint, JSONObject body, Response.Listener responseCallback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, serverUrl + endpoint, body,
     //This function is for Alex's use only
    private void sendPostRequestNz(String endpoint, JSONObject body, Response.Listener responseCallback, Response.ErrorListener errorCallback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, serverUrlNz + endpoint, body,
                responseCallback,
                errorCallback) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        requestQueue.add(request);

    }
    }*/

     
}
