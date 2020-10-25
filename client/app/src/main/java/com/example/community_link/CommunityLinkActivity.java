package com.example.community_link;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CommunityLinkActivity extends AppCompatActivity {
    private PopupWindow popup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View menu = inflater.inflate(R.layout.menu_layout, null);

        popup = new PopupWindow(menu);
        if (popup.isShowing()) {
            popup.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (popup.isShowing()) {
            popup.dismiss();
        }
    }

    public void toolbarMenu(View view) {

        popup.showAsDropDown(view);
        popup.update(600, 900);
        popup.setTouchable(true);
    }

    public void toolbarProfile(View view) {
        CharSequence message = "Sorry, this functionality (profile) isn't implemented yet";
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void home(View view) {
        if (!MainActivity.running) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        }
    }

    public void addService(View view) {
        Intent addService = new Intent(this, AddService2.class);
        startActivity(addService);
    }


    public void getService(View view) {
        Intent getService = new Intent(this, GetService.class);
        startActivity(getService);
    }

    public void requestExample(View view) {
        Intent requestExample = new Intent(this, RequestExample.class);
        startActivity(requestExample);
    }

    public void browseService(View view){
        Intent browseService = new Intent(this,BrowseServiceCond.class);
        startActivity(browseService);
    }

    public void enterChat(View view){
        Intent enterChat = new Intent(this, ChatActivity.class);
        startActivity(enterChat);
    }
}
