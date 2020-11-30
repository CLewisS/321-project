package com.example.community_link;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.BufferedReader;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ChatActivity extends CommunityLinkActivity implements AdapterView.OnItemSelectedListener {
    private Button sendButton;
    private EditText userInput;

    private Button addTargetButton;
    private EditText addTargetInput;

    private Spinner spinner;
    private List<String> targetNameList;    //Note: first item used as Hint only.

    //msg displays
    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;


    //User profiles are to be implemented as App global
    public String targetName;
    private boolean validTarget;
    public final String timeAnchor = "2020-09-01 12:12:12";
    public String lastUpdate = "2020-09-01 12:12:12";
    public String chat_target_pick_hint = "History:";

    //server IO portal used for chat Backend
    MyFirebaseMessagingService pushedMessageServer;

    //a local file storing the chat log
    private List<ChatMessage> chatLog;
    private Map<String, List<ChatMessage>> MasterChatLog;
    private File chatLogFile;
    public String chatDataLogFileSuffix = "_chatloglocal.tmp";

    //supportives for IO
    private Gson gson;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;

        //setup local user parameters
//        CommunityLinkApp.user = new UserProfile("TEST", "TEST");

        targetName = null;
        validTarget = false;
        
        MasterChatLog = new HashMap<String, List<ChatMessage>>();
        targetNameList = new ArrayList<String>();
        targetNameList.add(chat_target_pick_hint);

        //Firebase local token update
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
                        CommunityLinkApp.user.deviceToken = token;
                        // Log and toast
                        Log.d("UserProfile", token);
                        //Toast.makeText(ChatActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


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
                userInput.setText("");
                sendMessage(userMessage);
            }
        });

        //add target input box
        addTargetInput = findViewById(R.id.edittext_targetbox);
        addTargetInput.setText("");

        //add target confirm button
        addTargetButton = findViewById(R.id.button_targetbox_apply);
        addTargetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTarget = addTargetInput.getText().toString();
                addTargetInput.setText("");
                switchToNewTarget(newTarget);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent actIntent = getIntent();
        if (actIntent.hasExtra("targetName")) {
            targetName = actIntent.getStringExtra("targetName");
            EditText targetText = findViewById(R.id.edittext_targetbox);
            targetText.setText(targetName);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("pushNdata")
        );
        pushedMessageServer = new MyFirebaseMessagingService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Guards
        if(targetNameList == null || targetNameList.size() < 2|| targetNameList.get(1) == null){
            if(targetName == null){
                targetName = CommunityLinkApp.user.getUsername(); //initialization default to user self-Looping on first creation
                validTarget = true;
            }
            targetNameList = new ArrayList<String>();
            targetNameList.add(chat_target_pick_hint);
            targetNameList.add(targetName);
        }else{
            targetName = targetNameList.get(1);
            validTarget = true;
        }

        //launch chat view
        chatLog = new ArrayList<ChatMessage>();
        mergeChat(chatLog, loadFromFile(targetName));
        if(MasterChatLog.containsKey(targetName) && MasterChatLog.get(targetName) != null){
            mergeChat(chatLog, MasterChatLog.get(targetName).toArray(new ChatMessage[0]));
        }
        displayNow();

        //background resync
        checkForUpdate();

        //handles the push notification
        if(getIntent().getStringExtra("pushNdata") != null){
            String newPushedString = getIntent().getStringExtra("pushNdata");
            ChatMessage newPushedMessage = gson.fromJson(newPushedString, ChatMessage.class);
            if(newPushedMessage == null){ return;}  //abort on Hard errors

            if(newPushedMessage.sender.equals(targetName)){
                putAndOrder(new ChatMessage[]{newPushedMessage});
            }else{
                //update target name list
                if(!targetNameList.contains(newPushedMessage.sender)){
                    targetNameList.add(newPushedMessage.sender);
                }
                //then insert received message to its chatlog.
                if(!MasterChatLog.containsKey(newPushedMessage.sender)){
                    MasterChatLog.put(newPushedMessage.sender, null);
                }
                mergeChat(MasterChatLog.get(newPushedMessage.sender),new ChatMessage[]{newPushedMessage});
            }
            getIntent().removeExtra("pushNdata");
        }
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

    //Spinner: callback on user select
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(position == 0){
            return;
        }
        switchToNewTarget(targetNameList.get(position));
    }

    //Spinner: callback on user abort
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //change is if special action on user abort is needed.
    }

    //Background Message receiving callback
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChatMessage newMessage = gson.fromJson(intent.getExtras().getString("pushNdata"), ChatMessage.class);
            if(newMessage != null){
                if(newMessage.sender.equals(targetName)){
                    putAndOrder(new ChatMessage[]{newMessage});
                }else{
                    //if its an background update
                    if(!targetNameList.contains(newMessage.sender)){
                        targetNameList.add(newMessage.sender);
                    }
                    if(!MasterChatLog.containsKey(newMessage.sender)){
                        MasterChatLog.put(newMessage.sender, null);
                    }
                    mergeChat(MasterChatLog.get(newMessage.sender), new ChatMessage[]{newMessage});
                }
            }
            intent.removeExtra("pushNdata");
        }
    };

    //used for quick check for fast-forward updates
    public void checkForUpdate(){
        reSyncPull(lastUpdate);
    }

    //Http GET request for re-Pulling chats
    public void reSyncPull(String fromTime) {
        //a fix for the ServerDB timestamp format
        if(fromTime.length()>19){
            fromTime = fromTime.substring(0, 19);
        }

        //format request message
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("user1", CommunityLinkApp.user.getUsername());
            jsonMessage.put("user2", targetName);
            jsonMessage.put("timestamp", fromTime);
            Log.i("JSON", jsonMessage.toString());

            //http GET
            Response.Listener getMessageResponseCallback = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("response", response.toString());
                    //decode and put newly gotten messages to the chat list
                    ChatMessage[] newMessages = gson.fromJson(response.toString(), ChatMessage[].class);
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
            CommunityLinkApp.requestManager.getMessages(jsonMessage, getMessageResponseCallback, errorCallback);
        }catch(Exception e){
            Log.i("ChatGET", "Get failed");
            e.printStackTrace();
        }
    }

    //function to generate and send message as a JSON object to server
    public void sendMessage(String message) {
        //guard for empty sends
        if(message == null || message.equals("") || message.equals(" ")){return;}
        if(!validTarget){
            Toast.makeText(ChatActivity.this, "Recipient does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        //setting up basic local elements
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ChatMessage localMessage = new ChatMessage(CommunityLinkApp.user.getUsername(), targetName, time, message);

        //self Messaging is detached from the server, being local-only.
        if(targetName.equals(CommunityLinkApp.user.getUsername())){
            putAndOrder(new ChatMessage[]{localMessage});
            return;
        }

        //send the JSON Message
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("sender", CommunityLinkApp.user.getUsername());
            jsonMessage.put("recipient", targetName);
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
                    String resBody = new String(error.networkResponse.data);
                    try {
                        JSONObject res = new JSONObject(resBody);
                        if("ERR_NO_RECIPIENT".equals(res.getString("message"))) {
                            CharSequence toastMess = "Message wasn't sent\n\nUser " + targetName + " doesn't exist";
                            Toast toast = Toast.makeText(context, toastMess, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            validTarget = false;
                        }else{
                            Toast.makeText(ChatActivity.this, "Server unavailable, please try later", Toast.LENGTH_SHORT).show();
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("chat:sendMessage: HTTP POST response didn't work");
                    System.out.println(error.toString());
                }
            };
            CommunityLinkApp.requestManager.addMessage(jsonMessage, addMessageResponseCallback, errorCallback);
        } catch (JSONException e) {
            System.out.println("chat:sendMessage: JSON components malfunctions");
            e.printStackTrace();
            return;
        }

        //adding sent message to display
        putAndOrder(new ChatMessage[]{localMessage});

        //do a quick check for mis-aligned server state
        //checkForUpdate();
    }

    //call this function to switch to an new chat channel specified by newTargetName.
    public void switchToNewTarget(String newTargetName){
        //common error cases
        if(newTargetName == null || newTargetName.equals("")){
            Toast.makeText(ChatActivity.this, "Chat recipient cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newTargetName.equals(targetName)){
            Toast.makeText(ChatActivity.this, "Already on channel with "+targetName, Toast.LENGTH_SHORT).show();
            return;
        }

        //save previous session before switching
        updateLog();
        //clean up container for background updates
        MasterChatLog.put(targetName, null);

        //give users a direct sense of switching
        addTargetInput.setHint(newTargetName);
        addTargetInput.setText("");

        //add initializations if not exist already
        if(!targetNameList.contains(newTargetName)){
            targetNameList.add(newTargetName);
        }
        if(!MasterChatLog.containsKey(newTargetName)){
            MasterChatLog.put(newTargetName, null);
        }

        //element reSync with new parameters
        targetName = newTargetName;
        validTarget = true;
        chatLog = new ArrayList<ChatMessage>();
        mergeChat(chatLog, loadFromFile(newTargetName));
        if(MasterChatLog.containsKey(newTargetName) && MasterChatLog.get(newTargetName) != null){
            mergeChat(chatLog, MasterChatLog.get(newTargetName).toArray(new ChatMessage[0]));
        }
        displayNow();
        CharSequence toastMess = "Chatting with: "+ targetName;
        Toast toast = Toast.makeText(getApplicationContext(), toastMess, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //background chat resync
        if(chatLog == null || chatLog.size()<1){
            chatLog = new ArrayList<ChatMessage>();
            reSyncPull(timeAnchor);
        }else{
            reSyncPull(chatLog.get(0).timestamp);
        }
    }

    //function for properly ordering the chat entries and display them
    public void putAndOrder(ChatMessage[] newMessages){
        mergeChat(chatLog, newMessages);
        displayNow();
    }

    //A background Version of PutAndOrder(), for background sorts. Base should already sorted
    public void mergeChat(List<ChatMessage> base, ChatMessage[] newMessages){
        if(newMessages == null || newMessages.length == 0){
            return;
        }

        ArrayList<ChatMessage> receivedMessages = new ArrayList<ChatMessage>(Arrays.asList(newMessages));
        //correction for timestamp formatting
        for (int i=0; i<receivedMessages.size(); i++) {
            if (receivedMessages.get(i).timestamp.length() > 19) {
                receivedMessages.get(i).timestamp = receivedMessages.get(i).timestamp.substring(0, 19);
            }
        }

        if(base == null){
            base = new ArrayList<ChatMessage>();
        }

        if(base.size() == 0){
            base.add(receivedMessages.get(0));
            receivedMessages.remove(0);
        }
        for(int j=0; j<base.size(); j++){
            for(int i=0; i<receivedMessages.size(); i++){
                if(receivedMessages.get(i).compactString().equals(base.get(j).compactString())){
                    receivedMessages.remove(i);
                    i--;
                }else{
                    if(receivedMessages.get(i).timestamp.compareTo(base.get(j).timestamp) < 0){
                        base.add(j, receivedMessages.get(i));
                        receivedMessages.remove(i);
                        i--;
                    }
                }

            }
            if(receivedMessages.size() != 0 && j == base.size()-1){
                if(!receivedMessages.get(receivedMessages.size()-1).compactString().equals(base.get(j).compactString())){
                    base.add(receivedMessages.get(receivedMessages.size()-1));
                }
                receivedMessages.remove(receivedMessages.size()-1);
            }
            if(receivedMessages.size() == 0){
                break;
            }
        }
    }

    //the function used to put current chat history into the local cache
    public void updateLog() {
        if(!validTarget || targetName == null){
            System.out.println("Chat: NULL chat target, cache file not needed");
            return;
        }
        if(chatLog == null || chatLog.size() == 0){
            System.out.println("Chat: empty chat log, cache file not needed");
            return;
        }

        chatLogFile = new File(context.getFilesDir(), targetName + chatDataLogFileSuffix);
        try {
            //Create a new cache file in case of missing
            if(chatLogFile.createNewFile()) System.out.println("Chat: chat log file created");
            else System.out.println("Chat: chat log file found");

            //write the chat history to cache file
            FileOutputStream fout = context.openFileOutput(targetName + chatDataLogFileSuffix, Context.MODE_PRIVATE);
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

    //load local chatlog file of specified target, returns null if file not exists.
    public ChatMessage[] loadFromFile(String targetName){
        ChatMessage[] readFromFile = null;

        chatLogFile = new File(context.getFilesDir(), targetName + chatDataLogFileSuffix);
        if(chatLogFile.exists()){
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
                readFromFile = gson.fromJson(readString, ChatMessage[].class);
            } catch (FileNotFoundException e) {
                System.out.println("Chat:OnCreate chat log file exists but not recognized");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Chat:OnCreate chat log file exists but IO error-ed");
                e.printStackTrace();
            }
        }
        return readFromFile;
    }

    //display using chatlog and configure lastUpdate according to the chatlog
    public void displayNow(){
        //guard
        if(chatLog == null) {chatLog = new ArrayList<ChatMessage>();}

        //chat view
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatRecycleViewAdapter(chatLog, CommunityLinkApp.user.getUsername());

        int latestChatPosition = 0;
        if(chatLog.size()>0){
            lastUpdate = chatLog.get(chatLog.size()-1).timestamp;
            latestChatPosition = chatLog.size() - 1;
        }
        chatAdapter.notifyItemInserted(latestChatPosition);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.scrollToPosition(latestChatPosition);

        spinner = (Spinner) findViewById(R.id.chat_Target_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, targetNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
    }

    //this is the supporting function for toolbar "return to main" button
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}