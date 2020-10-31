package com.example.community_link;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends CommunityLinkActivity {
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    private void clearErrs() {
        TextView userErr = findViewById(R.id.userErrSignup);
        userErr.setText("");
        TextView passErr = findViewById(R.id.passErrSignup);
        passErr.setText("");
    }

    private boolean setErrs() {
        EditText usernameInput = (EditText) findViewById(R.id.usernameSignup);
        String username = usernameInput.getText().toString();

        EditText pass1 = (EditText) findViewById(R.id.passwordSignup);
        String password1 = pass1.getText().toString();

        EditText pass2 = (EditText) findViewById(R.id.passwordSignup2);
        String password2 = pass2.getText().toString();

        boolean err = false;
        clearErrs();

        if (username.isEmpty()) {
            TextView userErr = findViewById(R.id.userErrSignup);
            userErr.setText("Please enter a username.");
            err = true;
        }

        TextView passErr = findViewById(R.id.passErrSignup);
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
        EditText usernameInput = (EditText) findViewById(R.id.usernameSignup);
        final String username = usernameInput.getText().toString();

        EditText pass1 = (EditText) findViewById(R.id.passwordSignup);
        String password = pass1.getText().toString();

        try {
            JSONObject user = new JSONObject();
            user.put("username", username);
            user.put("password", password);
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
                    Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_SHORT).show();
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

        if(setErrs()) {
            Toast.makeText(getApplicationContext(), "Some fields have errors.", Toast.LENGTH_SHORT).show();
        } else {
            addUser();
        }

    }
}