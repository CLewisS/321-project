package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SearchResult extends AppCompatActivity {

    private List<ServiceData> sdList = new ArrayList<ServiceData>();
    private int i = 0;
    private TextView txv;

    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        /*<--------------HARDCODING----------------------->*/
        ServiceData sd1 = new ServiceData();
        sd1.setDescription("Tian'an Men Silence");
        sd1.setType("Parade");
        sd1.setDate(1989,6,4);
        sd1.setName("Anti-China Terrorists");
        sd1.setEventName("Silence");
        sd1.setTime(0,0);

        ServiceData sd2 = new ServiceData();
        sd2.setDescription("Tian'an Men Cleaning");
        sd2.setType("Cleaning");
        sd2.setDate(1989,6,4);
        sd2.setName("Jiang Zemin");
        sd2.setEventName("Clean");
        sd2.setTime(0,0);

        sdList.add(sd1);
        sdList.add(sd2);
        /*<--------------HARDCODING----------------------->*/

        txv = (TextView) findViewById(R.id.owner);
        txv.setText("Owner");
    }

    public void goPrev(View view){

        if(i!=0){
            i--;
        }
        txv.setText(sdList.get(i).toString());

    }

    public void goNext(View view){
        if(i != 1) {
            i++;
        }
        txv.setText(sdList.get(i).toString());
    }


}