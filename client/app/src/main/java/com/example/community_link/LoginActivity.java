package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends CommunityLinkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void clearErrs() {
        TextView userErr = findViewById(R.id.userErrLogin);
        userErr.setText("");
        TextView passErr = findViewById(R.id.passErrLogin);
        passErr.setText("");
    }

    private boolean setErrs() {
        EditText usernameInput = (EditText) findViewById(R.id.usernameLogin);
        String username = usernameInput.getText().toString();

        EditText pass = (EditText) findViewById(R.id.passwordLogin);
        String password = pass.getText().toString();


        boolean err = false;
        clearErrs();

        if (username.isEmpty()) {
            TextView userErr = findViewById(R.id.userErrLogin);
            userErr.setText("Please enter a username.");
            err = true;
        }

        TextView passErr = findViewById(R.id.passErrLogin);
        if (password.isEmpty()) {
            passErr.setText("Please enter a password.");
            err = true;
        }

        return err;
    }

    public void login(View view) {
        EditText usernameInput = (EditText) findViewById(R.id.usernameLogin);
        String username = usernameInput.getText().toString();

        EditText pass = (EditText) findViewById(R.id.passwordLogin);
        String password = pass.getText().toString();

        if(setErrs()) {
            Toast.makeText(getApplicationContext(), "Some fields have errors.", Toast.LENGTH_SHORT).show();
        } else {
            CommunityLinkApp.login(username, password);
        }

    }
}