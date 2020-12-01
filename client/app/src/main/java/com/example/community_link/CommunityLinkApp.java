package com.example.community_link;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class CommunityLinkApp extends Application {
    public static RequestManager requestManager;
    public static UserProfile user;
    private static boolean loggedIn;
    private static final String TAG = "APP";
    private static Context context;
    private static CommunityLinkActivity currActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DiskBasedCache requestCache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        requestManager = new RequestManager(requestCache);
        loggedIn = false;
    }

    public static void setCurrActivity(CommunityLinkActivity activity) {
        currActivity = activity;
    }


    public static boolean userLoggedIn() {
        return loggedIn;
    }

    /*
    * Checks if the logged in user has the specified username and password
    */
    public static boolean userIs(String username, String password) {
        if (loggedIn && user != null) {
            return username.equals(user.getUsername()) && password.equals(user.getPassword());
        }

        return false;
    }

    public static void setNewUser(String username, String password) {
        user = new UserProfile(username, password);
        loggedIn = true;

        CharSequence toastMess = "Successfully logged in";
        Toast toast = Toast.makeText(context, toastMess, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void removeCurrUser() {
        user = null;
        loggedIn = false;

        if (currActivity.getClass().equals(MainActivity.class)) {
            ((MainActivity) currActivity).setIntro();
        }

        CharSequence toastMess = "Successfully logged out";
        Toast toast = Toast.makeText(context, toastMess, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /* Used by the login function to check if the username and password are correct
     * It sends a request to the server.
     */
    private static void serverAuth(final String username, final String password, String token) {
        try {
            JSONObject user = new JSONObject();
            user.put("username", username);
            user.put("password", password);
            user.put("deviceToken", token);

            Response.Listener serverAuthCallback = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.w(TAG, "User authenticated");
                    try {
                        setNewUser(response.getString("username"), response.getString("password"));
                        currActivity.clearPopups(0);
                        if (currActivity.getClass().equals(MainActivity.class)) {
                            ((MainActivity) currActivity).setUserView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorCallback = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof AuthFailureError) {
                        Log.w(TAG, "User not authenticated.");
                        CharSequence toastMess = "Username and/or password incorrect";
                        Toast toast = Toast.makeText(context, toastMess, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        System.out.println("HTTP response didn't work");
                        System.out.println(error.toString());

                        CharSequence errorMess = "Sorry, we can't login at the moment.";
                        Toast errorToast = Toast.makeText(context, errorMess, Toast.LENGTH_LONG);
                        errorToast.setGravity(Gravity.CENTER, 0, 0);
                        errorToast.show();
                    }

                }
            };

            CommunityLinkApp.requestManager.authenticateUser(user, serverAuthCallback, errorCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void serverLogout(){
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("username", user.getUsername());
            userJSON.put("password", user.getPassword());
            userJSON.put("deviceToken", "");

            Response.Listener updateUserCallback = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.w(TAG, "User Logged out");
                    removeCurrUser();
                }
            };

            Response.ErrorListener errorCallback = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w(TAG, "User Log out problem.");
                    System.out.println("HTTP response didn't work");
                    System.out.println(error.toString());
                }
            };

            CommunityLinkApp.requestManager.updateUser(userJSON, updateUserCallback, errorCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Gets the FCM registration token and checks if the username and password are correct
     */
    public static void login(final String username, final String password) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            serverAuth(username, password, "");
                        } else {
                            serverAuth(username, password, task.getResult());
                            Log.d(TAG, task.getResult());
                        }

                    }
                });

        if (userLoggedIn()) {
            Log.w(TAG, "Logged in " + user.getUsername() + " " + user.getPassword());
        }

    }

    public static void logout() {
        if(userLoggedIn()) {
            serverLogout();
        } else {
            Log.w(TAG, "Error: Tried to log out user when no user was logged in.");
        }
    }

}
