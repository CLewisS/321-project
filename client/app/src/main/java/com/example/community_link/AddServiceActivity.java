package com.example.community_link;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class AddServiceActivity extends CommunityLinkActivity {
    private ServiceData sd = new ServiceData();
    private DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        initTime();
    }

    private void initTime() {
        // hour spinner
        Spinner hour = (Spinner) findViewById(R.id.hourAdd);
        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hour.setAdapter(hourAdapter);

        // min spinner
        Spinner min = (Spinner) findViewById(R.id.minAdd);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mins_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        min.setAdapter(adapter);
    }

    private int getHour() {
        Spinner hourSpin = findViewById(R.id.hourAdd);
        String hour = hourSpin.getSelectedItem().toString();

        if("hh".equals(hour)) {
            return -1;
        }

        return Integer.parseInt(hour);
    }

    private int getMin() {
        Spinner minSpin = findViewById(R.id.minAdd);
        String min = minSpin.getSelectedItem().toString();

        if("mm".equals(min)) {
            return -1;
        }

        return Integer.parseInt(min);
    }

    private void clearErrs() {
        TextView titleErr = findViewById(R.id.titleErr);
        titleErr.setText("");

        TextView typeErr = findViewById(R.id.typeErr);
        typeErr.setText("");

        TextView descErr = findViewById(R.id.descriptionErr);
        descErr.setText("");

        TextView dateErr = findViewById(R.id.dateErr);
        dateErr.setText("");

        TextView timeErr = findViewById(R.id.timeErr);
        timeErr.setText("");
    }

    public void submit(View view){
        int hour = getHour();
        int minute = getMin();

        EditText titleIn = (EditText) findViewById(R.id.etProjectName);
        String title = titleIn.getText().toString();

        EditText typeIn = (EditText) findViewById(R.id.etType);
        String type = typeIn.getText().toString();

        EditText editDesc = (EditText) findViewById(R.id.etDesc);
        String desc = editDesc.getText().toString();

        boolean err = false;
        clearErrs();

        if (title.isEmpty()) {
            TextView titleErr = findViewById(R.id.titleErr);
            titleErr.setText("Please set the title.");
            err = true;
        }

        if (type.isEmpty()) {
            TextView typeErr = findViewById(R.id.typeErr);
            typeErr.setText("Please set the type.");
            err = true;
        }

        if (desc.isEmpty()) {
            TextView descErr = findViewById(R.id.descriptionErr);
            descErr.setText("Please set the description.");
            err = true;
        }

        if (hour == -1 || minute == -1){
            TextView timeErr = findViewById(R.id.timeErr);
            timeErr.setText("Please set the time.");
            err = true;

        }

        if (err) {
            Toast.makeText(getApplicationContext(), "Some fields are missing information.", Toast.LENGTH_SHORT).show();
        } else {
            sd.setTime(hour, minute);
            sd.setEventName(title);
            sd.setName("Jiang Zeming");
            sd.setType(type);
            sd.setDescription(desc);

            picker = (DatePicker) findViewById(R.id.datePicker);
            sd.setDate(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());

            postToServer();

            CharSequence toastMess = "Your service was added!";
            Toast toast = Toast.makeText(view.getContext(), toastMess, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void cancel(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    /**To Enable the postToServer() Function,
     * Uncomment the postToServer() above,
     **/

    private void postToServer() {

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