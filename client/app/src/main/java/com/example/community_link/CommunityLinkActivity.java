package com.example.community_link;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CommunityLinkActivity extends AppCompatActivity {
    private PopupWindow navPopup;
    private PopupWindow profilePopup;
    private PopupWindow loginPopup;
    private View profileMenu;
    private View loginView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View menu = inflater.inflate(R.layout.menu_layout, null);
        profileMenu = inflater.inflate(R.layout.profile_menu_layout, null);
        loginView = inflater.inflate(R.layout.login_layout, null);

        navPopup = new PopupWindow(menu);
        profilePopup = new PopupWindow(profileMenu);
        loginPopup = new PopupWindow(loginView);
        clearPopups();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearPopups();
    }

    private void clearPopups() {
        if (navPopup.isShowing()) {
            navPopup.dismiss();
        }

        if (profilePopup.isShowing()) {
            profilePopup.dismiss();
        }

        if (loginPopup.isShowing()) {
            loginPopup.dismiss();
            clearLoginErrs();
            clearLoginInput();
        }
    }

    public void toolbarMenu(View view) {
        if (navPopup.isShowing()) {
            navPopup.dismiss();
        } else {
            navPopup.showAsDropDown(view);
            navPopup.update(600, 900);
            navPopup.setTouchable(true);
        }
    }

    public void toolbarProfile(View view) {
        if (profilePopup.isShowing()) {
            profilePopup.dismiss();
        } else {
            setProfileMenu();

            profilePopup.showAsDropDown(view, -400, 0);
            profilePopup.update(600, 400);
            profilePopup.setTouchable(true);
        }

    }

    private void setProfileMenu() {
        ViewGroup topView = (ViewGroup) profileMenu.getParent();
        TextView profileName = profileMenu.findViewById(R.id.profileName);
        Button signupButt = profileMenu.findViewById(R.id.menuSignupButt);
        Button loginButt = profileMenu.findViewById(R.id.menuLoginButt);
        Button logoutButt = profileMenu.findViewById(R.id.menuLogoutButt);

        if(CommunityLinkApp.userLoggedIn()) {
            profileName.setText(CommunityLinkApp.user.getUsername());
            signupButt.setVisibility(View.GONE);
            loginButt.setVisibility(View.GONE);
            logoutButt.setVisibility(View.VISIBLE);
        } else {
            signupButt.setVisibility(View.VISIBLE);
            loginButt.setVisibility(View.VISIBLE);
            logoutButt.setVisibility(View.GONE);
            profileName.setText("Not logged in");
        }
    }

    public void home(View view) {
        if (!MainActivity.running) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        }
    }

    public void addService(View view) {
        Intent addService = new Intent(this, AddServiceActivity.class);
        startActivity(addService);
    }


    public void requestExample(View view) {
        Intent requestExample = new Intent(this, RequestExample.class);
        startActivity(requestExample);
    }

    public void browseService(View view){
        Intent browseService = new Intent(this,BrowseActivity.class);
        startActivity(browseService);
    }

    public void enterChat(View view){
        Intent enterChat = new Intent(this, ChatActivity.class);
        startActivity(enterChat);
    }

    public void signup(View view){
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
    }

    public void loginPopup(View view){
        if(profilePopup.isShowing()) {
            profilePopup.dismiss();
        }

        View navbar = findViewById(R.id.navbar);
        loginPopup.showAsDropDown(navbar);
        loginPopup.setTouchable(true);
        loginPopup.setFocusable(true);
        loginPopup.update(navbar.getWidth(), 600);

    }

    public void logout(View view){
        CommunityLinkApp.logout();
        Log.w("Log out", "Clearing popups");
        clearPopups();
    }

    /* Log in popup code */

    private  void clearLoginInput() {
        EditText usernameInput = (EditText) loginView.findViewById(R.id.usernameLogin);
        EditText pass = (EditText) loginView.findViewById(R.id.passwordLogin);

        usernameInput.setText("");
        pass.setText("");
    }
    private void clearLoginErrs() {
        TextView userErr = loginView.findViewById(R.id.userErrLogin);
        userErr.setText("");
        TextView passErr = loginView.findViewById(R.id.passErrLogin);
        passErr.setText("");
    }

    private boolean setLoginErrs() {
        EditText usernameInput = (EditText) loginView.findViewById(R.id.usernameLogin);
        String username = usernameInput.getText().toString();

        EditText pass = (EditText) loginView.findViewById(R.id.passwordLogin);
        String password = pass.getText().toString();


        boolean err = false;
        clearLoginErrs();

        if (username.isEmpty()) {
            TextView userErr = loginView.findViewById(R.id.userErrLogin);
            userErr.setText("Please enter a username.");
            err = true;
        }

        TextView passErr = loginView.findViewById(R.id.passErrLogin);
        if (password.isEmpty()) {
            passErr.setText("Please enter a password.");
            err = true;
        }

        return err;
    }

    public void login(View view) {
        EditText usernameInput = (EditText) loginView.findViewById(R.id.usernameLogin);
        String username = usernameInput.getText().toString();

        EditText pass = (EditText) loginView.findViewById(R.id.passwordLogin);
        String password = pass.getText().toString();

        if(setLoginErrs()) {
            Toast.makeText(getApplicationContext(), "Some fields have errors.", Toast.LENGTH_SHORT).show();
        } else {
            CommunityLinkApp.login(username, password);
            clearPopups();
        }

    }
}
