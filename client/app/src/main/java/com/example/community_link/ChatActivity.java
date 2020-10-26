package com.example.community_link;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class ChatActivity extends CommunityLinkActivity {
    private Button sendButton;
    private EditText userInput;

    //msg displays
    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;


    //TODO: fix these globals
    //User profiles are to be implemented as App global
    public userProfile user;
    public userProfile target;
    public String lastUpdate = "0";

    //server IO portal used for chat Backend
    private DiskBasedCache chatNetCache;
    RequestManager chatPortal;
    MyFirebaseMessagingService pushedMessageServer;

    //a local file storing the chat log
    private List<chatMessage> chatLog;
    private File chatLogFile;
    public String chatDataLogFile = "chatloglocal.tmp";

    //supportives for IO
    private Gson gson;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;

        //setup local user parameters
        //TODO: Config these to use the real runtime data
        user = new userProfile("else");
        target = new userProfile("local");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("UserProfile", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult() + " ------------------------------------------";
                        user.deviceToken = token;
                        // Log and toast
                        Log.d("UserProfile", token);
                        Toast.makeText(ChatActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        //network parameters
        chatNetCache = new DiskBasedCache(context.getCacheDir());
        chatPortal = new RequestManager(chatNetCache);


        //Json library (Gson helper)
        gson = new Gson();

        //input text box
        userInput = findViewById(R.id.edittext_chatbox);
        userInput.setText("");

        //send button
        sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = userInput.getText().toString();
                sendMessage(userMessage);
                userInput.setText("");
            }
        });

        //chat view setup
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatLog = new ArrayList<chatMessage>();
        chatAdapter = new chatRecycleViewAdapter(chatLog, user.id);
        recyclerView.setAdapter(chatAdapter);

//        //TODO: get rid of these inline testing stuff later
//        //local testing purposes----
//        chatLog.add(new chatMessage(user.id, target.id, "000", "Hi, this is" + user.id));
//        chatLog.add(new chatMessage(target.id, user.id, "001", "Hi, this is" + target.id));
//        //END of TEST----

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("pushNdata")
        );
        pushedMessageServer = new MyFirebaseMessagingService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(chatLog == null){
            chatLog = new ArrayList<chatMessage>();
        }

        //load the past chat log from the app local storage/ create one if not available.
        chatLogFile = new File(context.getFilesDir(), chatDataLogFile);
        if(!chatLogFile.exists()){;
        }else{
            //if the chatLog cache exists, go read it and populate the chatlog List.
            try {
                FileInputStream fin = new FileInputStream(chatLogFile.getPath());
                InputStreamReader read = new InputStreamReader(fin);
                BufferedReader buffreader = new BufferedReader (read) ;
                StringBuilder sb = new StringBuilder();
                String readString = buffreader.readLine() ;
                while ( readString != null ) {
                    sb.append(readString);
                    readString = buffreader.readLine() ;
                }
                read.close();
                readString = sb.toString();
                chatMessage[] holder = gson.fromJson(readString, chatMessage[].class);
                chatLog = new ArrayList<chatMessage>(Arrays.asList(holder));
            } catch (FileNotFoundException e) {
                System.out.println("Chat:OnCreate chat log file exists but not recognized");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Chat:OnCreate chat log file exists but IO error-ed");
                e.printStackTrace();
            }
        }

        //chat view
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new chatRecycleViewAdapter(chatLog, user.id);

        int latestChatPosition = 0;
        if(chatLog.size()>0){
            lastUpdate = chatLog.get(chatLog.size()-1).timestamp;
            latestChatPosition = chatLog.size() - 1;
        }
        chatAdapter.notifyItemInserted(latestChatPosition);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.smoothScrollToPosition(latestChatPosition);

        //handles the push notification
        if(getIntent().getStringExtra("pushNdata") != null){
            String newPushedString = getIntent().getStringExtra("pushNdata");
            chatMessage newPushedMessage = gson.fromJson(newPushedString, chatMessage.class);
            putAndOrder(new chatMessage[]{newPushedMessage});
        }
        checkForUpdate();

        //push notification setup at last when everything is ready

    }

    @Override
    protected void onPause() {
        super.onPause();

        updateLog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            chatMessage newMessage = gson.fromJson(intent.getExtras().getString("pushNdata"), chatMessage.class);
            if(newMessage != null){
                putAndOrder(new chatMessage[]{newMessage});
            }
            intent.removeExtra("pushNdata");
        }
    };

    //Http GET request for updating chats
    public void checkForUpdate() {
        //format request message
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("user1", user.id);
            jsonMessage.put("user2", target.id);
            jsonMessage.put("timestamp", lastUpdate);
            Log.i("JSON", jsonMessage.toString());

            //http GET
            Response.Listener getMessageResponseCallback = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("response", response.toString());
                    //decode and put newly gotten messages to the chat list
                    chatMessage[] newMessages = gson.fromJson(response.toString(), chatMessage[].class);
                    putAndOrder(newMessages);
                }
            };
            Response.ErrorListener errorCallback = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("chat:checkForUpdate: HTTP GET response didn't work");
                    System.out.println(error.toString());
                }
            };
            chatPortal.getMessages(jsonMessage, getMessageResponseCallback, errorCallback);
        }catch(Exception e){
            Log.i("ChatGET", "Get failed");
            e.printStackTrace();
        }
    }

    //function to generate and send message as a JSON object to server
    public void sendMessage(String message) {
        //guard for empty sends
        if(message.equals("") || message.equals(" ")){return;}

        //setting up basic local elements
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        chatMessage localMessage = new chatMessage(user.id, target.id, time, message);

        //send the JSON Message
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("sender", user.id);
            jsonMessage.put("recipient", target.id);
            jsonMessage.put("timestamp", time);
            jsonMessage.put("content", message);
            Log.i("JSON", jsonMessage.toString());

            //http POST
            Response.Listener addMessageResponseCallback = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response", response.toString());
                }
            };
            Response.ErrorListener errorCallback = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("chat:sendMessage: HTTP POST response didn't work");
                    System.out.println(error.toString());
                }
            };
            chatPortal.addMessage(jsonMessage, addMessageResponseCallback, errorCallback);
        } catch (JSONException e) {
            System.out.println("chat:sendMessage: JSON components malfunctions");
            e.printStackTrace();
        }

        //check for server updates
        //checkForUpdate();

        //adding successfully sent message to display
        putAndOrder(new chatMessage[]{localMessage});
    }

    //function for properly ordering the chat entries and display them
    public void putAndOrder(chatMessage[] newMessages){
        if(newMessages == null || newMessages.length == 0){
            return;
        }

        ArrayList<chatMessage> receivedMessages = new ArrayList<chatMessage>(Arrays.asList(newMessages));
        if(chatLog == null){
            chatLog = new ArrayList<chatMessage>();
        }

        if(chatLog.size() == 0){
            chatLog.add(receivedMessages.get(0));
            receivedMessages.remove(0);
        }
        for(int j=0; j<chatLog.size(); j++){
            for(int i=0; i<receivedMessages.size(); i++){
                if(receivedMessages.get(i).timestamp.compareTo(chatLog.get(j).timestamp) < 0){
                    chatLog.add(j, receivedMessages.get(i));
                    receivedMessages.remove(i);
                    i--;
                }
            }
            if(receivedMessages.size() != 0 && j == chatLog.size()-1){
                chatLog.add(receivedMessages.get(receivedMessages.size()-1));
                receivedMessages.remove(receivedMessages.size()-1);
            }
            if(receivedMessages.size() == 0){
                break;
            }
        }

        //updateLog();
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new chatRecycleViewAdapter(chatLog, user.id);

        lastUpdate = chatLog.get(chatLog.size()-1).timestamp;
        int latestChatPosition = chatLog.size() - 1;
        chatAdapter.notifyItemInserted(latestChatPosition);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.smoothScrollToPosition(latestChatPosition);
    }

    //the function used to put current chat history into the local cache
    public void updateLog() {
        if(chatLog == null || chatLog.size() == 0){
            System.out.println("Chat: empty chat log, cache file not needed");
            return;
        }

        chatLogFile = new File(context.getFilesDir(), chatDataLogFile);
        try {
            //Create a new cache file in case of missing
            if(chatLogFile.createNewFile()) System.out.println("Chat: chat log file created");
            else System.out.println("Chat: chat log file found");

            //write the chat history to cache file
            FileOutputStream fout = context.openFileOutput(chatDataLogFile, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fout);
            writer.write(gson.toJson(chatLog));
            writer.flush();
            writer.close();
            fout.close();
        } catch (FileNotFoundException e) {
            //this should not happen... but just in case, do something
            System.out.println("Chat:updateLog chat log file vanished while writing");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Chat:updateLog chat log file IO error-ed");
            e.printStackTrace();
        }
    }

    //this is the supporting function for toolbar "return to main" button
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}