package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class BrowseServiceCond extends AppCompatActivity {

    private Intent browseResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_service_cond);
        browseResult = new Intent(this,BrowseResult.class);

        /*<Hardcoding for now>*/

    }

    // This currently isn't doing anything
    public void date(View view) {
        DatePicker dp = (DatePicker) view;
        String date = dp.getYear() + "-" + (dp.getMonth() + 1) + "-" + dp.getDayOfMonth();

        if ("dateMin".equals(view.getId())) {
            browseResult.putExtra("date-min", date);
        } else if ("dateMax".equals(view.getId())) {
            browseResult.putExtra("date-max", date);
        }
    }

    public void browseResult(View view){

        // Adding search criteria to intent this is a mess right now
        EditText title = findViewById(R.id.titleInput);
        if (!title.getText().toString().isEmpty()) {
            browseResult.putExtra("title", title.getText().toString());
        }

        EditText dist = findViewById(R.id.distanceInput);
        System.out.println(dist.getText().toString());
        if (!dist.getText().toString().isEmpty()) {
            browseResult.putExtra("dist", Float.valueOf(dist.getText().toString()));
        }

        EditText currLat = findViewById(R.id.currLat);
        if (!currLat.getText().toString().isEmpty()) {
            browseResult.putExtra("currLat", Float.valueOf(currLat.getText().toString()));
        }

        EditText currLong = findViewById(R.id.currLong);
        if (!currLong.getText().toString().isEmpty()) {
            browseResult.putExtra("currLong", Float.valueOf(currLong.getText().toString()));
        }

        DatePicker dpMin = findViewById(R.id.dateMin);
        String dateMin = dpMin.getYear() + "-" + (dpMin.getMonth() + 1) + "-" + dpMin.getDayOfMonth();
        DatePicker dpMax = findViewById(R.id.dateMax);
        String dateMax = dpMax.getYear() + "-" + (dpMax.getMonth() + 1) + "-" + dpMax.getDayOfMonth();

        browseResult.putExtra("date-min", dateMin);
        browseResult.putExtra("date-max", dateMax);

        EditText timeMin = findViewById(R.id.timeMin);
        EditText timeMax = findViewById(R.id.timeMax);

        browseResult.putExtra("time-min", timeMin.getText().toString());
        browseResult.putExtra("time-max", timeMax.getText().toString());


        startActivity(browseResult);
    }


}