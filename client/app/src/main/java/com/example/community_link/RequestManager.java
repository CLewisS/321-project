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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class RequestManager {

    private DiskBasedCache cache;

    private BasicNetwork network;

    private RequestQueue requestQueue;

    private static final String serverUrl = "http://ec2-3-13-46-252.us-east-2.compute.amazonaws.com:8080"; // MUST BE SET TO SERVER URL

    public RequestManager(DiskBasedCache cache) {
        this.cache = cache;//new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        this.network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        this.requestQueue = new RequestQueue(cache, network);

        this.requestQueue.start();
    }

    public void getServices(JSONObject conditions, Response.Listener getServicesCallback, Response.ErrorListener getServicesErrorCallback) {
        StringBuilder endpoint = new StringBuilder();
        endpoint.append("/service?date-min=2020-10-15&date-max=2020-11-15&lat-min=49.3456&longi-max=123.456");

        sendGetRequest(endpoint.toString(), getServicesCallback, getServicesErrorCallback);
    }

    private void sendGetRequest(String endpoint, Response.Listener getServicesCallback, Response.ErrorListener getServicesErrorCallback) {

        // Formulate the request and handle the response.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, serverUrl + endpoint, null,
                getServicesCallback,
                getServicesErrorCallback);

        requestQueue.add(request);

    }

    private void sendPostRequest(String endpoint, Response.Listener getServicesCallback, Response.ErrorListener getServicesErrorCallback) {

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl + endpoint,
                getServicesCallback,
                getServicesErrorCallback);

        /*JSONObject jsonBody = new JSONObject();
        jsonBody.put("Title", "Android Volley Demo");
        jsonBody.put("Author", "BNK");
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                getServicesCallback,
                getServicesErrorCallback) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };*/

        requestQueue.add(stringRequest);

    }
}
