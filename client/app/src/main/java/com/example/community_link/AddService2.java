package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        TextView txv = findViewById(R.id.textView2);
        txv.setText(sd.toJSON());
        //postToServer();
    }

    /**To Enable the postToServer() Function,
     * Uncomment the postToServer Function, Replace the url with your url
     * Uncomment the postToServer() above,
     * Comment Line 37 & 38
     **/

    /*
    private void postToServer(){
       RequestQueue queue = Volley.newRequestQueue(this);
       String url ="http://nazokunvm.eastus.cloudapp.azure.com:8080/get";

       StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       TextView txv = findViewById(R.id.textView2);
                       txv.setText(response);
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               TextView txv = findViewById(R.id.textView2);
               txv.setText(error.getMessage());
           }
       }){
           @Override
           public byte[] getBody() throws AuthFailureError {
               String your_string_json = sd.toJSON();
               return your_string_json.getBytes();
           }
       };

       queue.add(stringRequest);
   }
 */

}