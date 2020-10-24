package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BrowseServiceCond extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_service_cond);
        /*<Hardcoding for now>*/

    }

    public void browseResult(View view){
        Intent browseResult = new Intent(this,BrowseResult.class);
        startActivity(browseResult);
    }


}