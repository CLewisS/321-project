package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;




public class GetService extends AppCompatActivity {

    private String url = "http://nazokunvm.eastus.cloudapp.azure.com:8080/testReading";
    private ServiceData serviceData = new ServiceData();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_service);
        /*<-----------------Hard code for now--------------------------->**/
        TextView txv = (TextView)findViewById(R.id.DescSearch);
        txv.setText("Based On your previous recording, \n" +
                    "We Suggest the following Searching Conditions: \n" +
                    "Type: Sorting");
    }

    public void searchResult(View view){
        Intent getResult = new Intent(this,SearchResult.class);
        startActivity(getResult);
    }



}