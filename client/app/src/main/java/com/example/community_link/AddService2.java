package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddService2 extends AppCompatActivity {

    ServiceData sd;
    DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service2);
        Intent addService2 = getIntent();
        sd = (ServiceData) addService2.getSerializableExtra("ServiceData");

    }

    public void test(View view){
        picker=(DatePicker)findViewById(R.id.datePicker1);
        sd.setDate(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
        //TextView txv = findViewById(R.id.textView2);
        //txv.setText(sd.toJSON());
        postToServer();
    }

    /**To Enable the postToServer() Function,
     * Uncomment the postToServer() above,
     **/

    private void postToServer(){
        final TextView txv = findViewById(R.id.textView2);
        JSONObject service = new JSONObject();
        try {
            service = new JSONObject(sd.toJSON());
        }catch(JSONException e) {
            e.printStackTrace();
        }

        Response.Listener addServiceResponseCallback = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                txv.setText(response.toString());
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.addService(service, addServiceResponseCallback, errorCallback);
   }

}