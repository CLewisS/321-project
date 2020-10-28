package com.example.community_link;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends CommunityLinkActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "MainActivity";
    public static Boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFCMToken();
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    private void getFCMToken() {
        System.out.println("Getting instance id");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult() + " ------------------------------------------";

                        // Log and toast
                        Log.d(TAG, token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Moved to CommunityLinkActivity class
/*
    public void toolbarMenu(View view) {
        CharSequence message = "Sorry, this functionality (menu) isn't implemented yet";
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        LayoutInflater inflater = LayoutInflater.from(this.getBaseContext());
        View menu = inflater.inflate(R.layout.menu_layout, null);
        PopupWindow popup = new PopupWindow(menu);
        popup.showAsDropDown(view);
        popup.update(325, 500);
        System.out.println("Should have popped up " + popup.isShowing() + " " + popup.getWidth() + " " + popup.getHeight());
    }

    public void toolbarProfile(View view) {
        CharSequence message = "Sorry, this functionality (profile) isn't implemented yet";
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void addService(View view) {
        Intent addService = new Intent(this, AddService.class);
        startActivity(addService);
    }


    public void getService(View view) {
        Intent getService = new Intent(this, GetService.class);
        startActivity(getService);
    }

    public void requestExample(View view) {
        Intent requestExample = new Intent(this, RequestExample.class);
        startActivity(requestExample);
    }*/

    public void browseService(View view){
        Intent browseService = new Intent(this,BrowseActivity.class);
        startActivity(browseService);
    }

    public void enterChat(View view){
        Intent enterChat = new Intent(this, ChatActivity.class);
        startActivity(enterChat);
    }


}