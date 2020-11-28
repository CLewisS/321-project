package com.example.community_link;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class CommunityLinkActivity extends AppCompatActivity {
    private final int ALL = 0;
    private final int EXCEPT_NAV = 1;
    private final int EXCEPT_PROFILE = 2;
    private final int EXCEPT_SIGNUP = 3;
    private final int EXCEPT_LOGIN = 4;

    private PopupWindow navPopup;
    private PopupWindow profilePopup;
    private PopupWindow loginPopup;
    private PopupWindow signupPopup;
    private View profileMenu;
    private View loginView;
    private View signupView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        View menu = inflater.inflate(R.layout.menu_layout, null);
        profileMenu = inflater.inflate(R.layout.profile_menu_layout, null);
        loginView = inflater.inflate(R.layout.login_layout, null);
        signupView = inflater.inflate(R.layout.signup_layout, null);

        navPopup = new PopupWindow(menu);
        profilePopup = new PopupWindow(profileMenu);
        loginPopup = new PopupWindow(loginView);
        signupPopup = new PopupWindow(signupView);
        clearPopups(ALL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CommunityLinkApp.setCurrActivity(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        clearPopups(ALL);
    }

    public void clearPopups(int except) {
        if (navPopup.isShowing() && except != EXCEPT_NAV) {
            navPopup.dismiss();
        }

        if (profilePopup.isShowing() && except != EXCEPT_PROFILE) {
            profilePopup.dismiss();
        }

        if (loginPopup.isShowing() && except != EXCEPT_LOGIN) {
            loginPopup.dismiss();
            clearLoginErrs();
            clearLoginInput();
        }

        if (signupPopup.isShowing() && except != EXCEPT_SIGNUP) {
            signupPopup.dismiss();
            clearSignupErrs();
            clearSignupInput();
        }
    }

    public void toolbarMenu(View view) {
        if(navPopup.isShowing()) {
            clearPopups(ALL);
        } else {
            clearPopups(EXCEPT_NAV);

            navPopup.showAsDropDown(view);
            navPopup.setTouchable(true);
            navPopup.update(600, 700);
        }
    }

    public void toolbarProfile(View view) {
        if(profilePopup.isShowing()) {
            clearPopups(ALL);
        } else {
            clearPopups(EXCEPT_PROFILE);
            setProfileMenu();

            profilePopup.showAsDropDown(view, -400, 0);
            profilePopup.setTouchable(true);
            profilePopup.update(600, 400);
        }
    }

    private void setProfileMenu() {
        //ViewGroup topView = (ViewGroup) profileMenu.getParent();
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
        if (CommunityLinkApp.userLoggedIn()) {
            Intent addService = new Intent(this, AddServiceActivity.class);
            startActivity(addService);
        } else {
            CharSequence toastMess = "Sorry, you must be logged in to Add a service.";
            Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

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
        if(CommunityLinkApp.userLoggedIn()){
            Intent enterChat = new Intent(this, ChatActivity.class);
            startActivity(enterChat);
        }else{
            Toast.makeText(this, "Please log-in or sign-up before using chat", Toast.LENGTH_SHORT).show();
        }
    }

    public void signupPopup(View view){
        if (signupPopup.isShowing()) {
            clearPopups(ALL);
        } else {
            clearPopups(EXCEPT_SIGNUP);
            clearSignupErrs();
            clearSignupInput();

            View navbar = findViewById(R.id.navbar);
            signupPopup.showAsDropDown(navbar);
            signupPopup.setTouchable(true);
            signupPopup.setFocusable(true);
            signupPopup.update(navbar.getWidth(), 800);
        }
    }

    public void loginPopup(View view){
        if (loginPopup.isShowing()) {
            clearPopups(ALL);
        } else {
            clearPopups(EXCEPT_LOGIN);
            clearLoginErrs();
            clearLoginInput();

            View navbar = findViewById(R.id.navbar);
            loginPopup.showAsDropDown(navbar);
            loginPopup.setTouchable(true);
            loginPopup.setFocusable(true);
            loginPopup.update(navbar.getWidth(), 600);
        }
    }

    public void logout(View view){
        CommunityLinkApp.logout();
        Log.w("Log out", "Clearing popups");
        clearPopups(ALL);
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
        String encyPass = encryPassword(password);

        if(setLoginErrs()) {
            Toast.makeText(getApplicationContext(), "Some fields have errors.", Toast.LENGTH_SHORT).show();
        } else {
            CommunityLinkApp.login(username, encyPass);
            //clearPopups(ALL);
        }
    }

    /* Sign up popup code */
    private void clearSignupInput() {
        EditText usernameInput = (EditText) signupView.findViewById(R.id.usernameSignup);
        EditText pass1 = (EditText) signupView.findViewById(R.id.passwordSignup);
        EditText pass2 = (EditText) signupView.findViewById(R.id.passwordSignup2);

        usernameInput.setText("");
        pass1.setText("");
        pass2.setText("");

    }

    private void clearSignupErrs() {
        TextView userErr = signupView.findViewById(R.id.userErrSignup);
        userErr.setText("");
        TextView passErr = signupView.findViewById(R.id.passErrSignup);
        passErr.setText("");
    }

    private boolean setSignupErrs() {
        EditText usernameInput = (EditText) signupView.findViewById(R.id.usernameSignup);
        String username = usernameInput.getText().toString();

        EditText pass1 = (EditText) signupView.findViewById(R.id.passwordSignup);
        String password1 = pass1.getText().toString();

        EditText pass2 = (EditText) signupView.findViewById(R.id.passwordSignup2);
        String password2 = pass2.getText().toString();

        boolean err = false;
        clearSignupErrs();

        if (username.isEmpty()) {
            TextView userErr = signupView.findViewById(R.id.userErrSignup);
            userErr.setText("Please enter a username.");
            err = true;
        } else if (username.length() >= 50) {
            TextView userErr = signupView.findViewById(R.id.userErrSignup);
            userErr.setText("Sorry, username must be shorter than 50 characters.");
            err = true;
        }

        TextView passErr = signupView.findViewById(R.id.passErrSignup);
        if (password1.isEmpty() || password2.isEmpty()) {
            passErr.setText("Please enter a password and confirmation.");
            err = true;
        } else if (!password1.equals(password2)) {
            passErr.setText("Password and confirmation don't match.");
            err = true;
        }

        return err;
    }

    /* Tries to add user with username and password to the server.
     * If a user with username already exists it doesn't add the user.
     */
    private void addUser() {
        EditText usernameInput = (EditText) signupView.findViewById(R.id.usernameSignup);
        final String username = usernameInput.getText().toString();

        EditText pass1 = (EditText) signupView.findViewById(R.id.passwordSignup);
        String password = pass1.getText().toString();
        String encyPass = encryPassword(password);

        try {
            JSONObject user = new JSONObject();
            user.put("username", username);
            user.put("password", encyPass);
            Response.Listener getServicesResponseCallback = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "User added successfully.", Toast.LENGTH_SHORT).show();
                    try {
                        CommunityLinkApp.login(response.getString("username"), response.getString("password"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            Response.ErrorListener errorCallback = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String resBody = new String(error.networkResponse.data);
                    try {
                        JSONObject res = new JSONObject(resBody);
                        if("USER_ALREADY_EXISTS".equals(res.getString("message"))) {
                            CharSequence toastMess = "Sorry, username already exists\nPlease choose a different username";
                            Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println("HTTP response didn't work");
                    System.out.println(error.toString());
                }
            };

            CommunityLinkApp.requestManager.addUser(user, getServicesResponseCallback, errorCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void signup(View view) {

        if(setSignupErrs()) {
            Toast.makeText(getApplicationContext(), "Some fields have errors.", Toast.LENGTH_SHORT).show();
        } else {
            addUser();
            clearPopups(ALL);
        }

    }

    private String encryPassword(String password) {
        MessageDigest messageDigest;
        StringBuilder sb = new StringBuilder(password);
        sb.append("IHateELEC221");
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(sb.toString().getBytes());
            byte[] messageDigestMD5 = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte bytes : messageDigestMD5) {
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "Wrong";
    }

}
