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
        txv.setText("Going to search for All of the available events");
    }

    public void searchResult(View view){
        Intent getResult = new Intent(this,SearchResult.class);
        startActivity(getResult);

        /*The prototype of GET*/
        /*
        String url = "OUR URL:PORT" + "/" + condition
        Pass the url to the new activity
        */
    }



}