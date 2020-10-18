package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

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
    }
}