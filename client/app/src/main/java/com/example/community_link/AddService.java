package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


public class AddService extends AppCompatActivity {
    private TimePicker picker;
    private Button btnGet;
    private TextView tvw;
     ServiceData sd = new ServiceData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        picker=(TimePicker)findViewById(R.id.datePicker1);
        picker.setIs24HourView(true);
        btnGet=(Button)findViewById(R.id.button2);
    }

    public void getTime(View view){
        int hour, minute;
        if (Build.VERSION.SDK_INT >= 23 ){
            hour = picker.getHour();
            minute = picker.getMinute();
        }
        else{
            hour = picker.getCurrentHour();
            minute = picker.getCurrentMinute();
        }
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName2);
        String message = editText.getText().toString();
        sd.setTime(hour,minute);
        sd.setEventName(message);
        sd.setName("Jiang Zeming");
        Intent addService2 = new Intent(this,AddService2.class);
        addService2.putExtra("ServiceData",sd);
        startActivity(addService2);
    }

}