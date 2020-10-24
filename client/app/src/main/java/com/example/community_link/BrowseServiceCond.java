package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BrowseServiceCond extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_service_cond);
        /*<Hardcoding for now>*/

    }

    public void browseResult(View view){
        EditText title = findViewById(R.id.titleInput);
        EditText dist = findViewById(R.id.distanceInput);
        EditText currLat = findViewById(R.id.currLat);
        EditText currLong = findViewById(R.id.currLong);

        Intent browseResult = new Intent(this,BrowseResult.class);
        browseResult.putExtra("title", title.getText().toString());
        browseResult.putExtra("dist", Float.valueOf(dist.getText().toString()));
        browseResult.putExtra("currLat", Float.valueOf(currLat.getText().toString()));
        browseResult.putExtra("currLong", Float.valueOf(currLong.getText().toString()));
        startActivity(browseResult);
    }


}