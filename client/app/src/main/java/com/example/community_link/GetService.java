package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class GetService extends AppCompatActivity {

    private String url = "http://nazokunvm.eastus.cloudapp.azure.com:8080/testReading";
    private ServiceData serviceData = new ServiceData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_service);
        /*<-----------------Hard code for now--------------------------->**/
    }

    public void searchResult(View view){
        Intent getResult = new Intent(this,SearchResult.class);
        startActivity(getResult);
    }

}