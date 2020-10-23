package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




public class ChatActivity extends AppCompatActivity {
    private List<chatMessage> messageList;
    private Button sendButton;
    private EditText userInput;

    //msg displays
    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<chatMessage> chatLog;


    //TODO: fix these globals
    //User profiles are to be implemented as App global
    public userProfile user;
    public userProfile target;
    public String lastupdate;

    //serverIP used for chat Backend
    private String serverIP;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //setup local user parameters
        //TODO: Config these to use the real runtime data
        user = new userProfile("local");
        target = new userProfile("else");
        serverIP = "0.0.0.0:3000";

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

        //chat view
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatLog = new ArrayList<chatMessage>();

        //TODO: get rid of these inline testing stuff later
        //local testing purposes----
        chatLog.add(new chatMessage("local", "else", "021", "Hello"));
        chatLog.add(new chatMessage("local", "else", "022", "Hello ha"));
        chatLog.add(new chatMessage("else", "local", "088", "what?"));
        //END of TEST----

        chatAdapter = new chatRecycleViewAdapter(chatLog, user.id);
        recyclerView.setAdapter(chatAdapter);
    }


    private void checkForUpdate() {
        //format request message
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("User1", user.id);
            jsonMessage.put("User2", target.id);
            jsonMessage.put("Newest", lastupdate);
            Log.i("JSON", jsonMessage.toString());

            //TODO: Http GET request and decode responses


        }catch(Exception e){
            Log.i("ChatGET", "Get failed");
            e.printStackTrace();
        }

        //TODO: implement incoming message logic
        //lastupdate = <newest timestamp>?
        //add to chatLog... maybe use a for loop for all new messages?
        /*
        //adding successful sent message to local chat list
        chatLog.add(localMessage);
        lastupdate = localMessage.timeStamp;
        int latestChatPosition = chatLog.size() - 1;
        chatAdapter.notifyItemInserted(latestChatPosition);
         */

    }

    //function to generate and send message as a JSON object to server
    private void sendMessage(String message) {
        //guard for empty sends
        if(message.equals("") || message.equals(" ")){return;}

        //setting up basic local elements
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        chatMessage localMessage = new chatMessage(user.id, target.id, time, message);

        //send the JSON Message
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("Sender", user.id);
            jsonMessage.put("Receiver", target.id);
            jsonMessage.put("TimeStamp", time);
            jsonMessage.put("Content", message);
            Log.i("JSON", jsonMessage.toString());

            //TODO: GET/POST probably incorrect anyway, fix them
            //use Http POST for message inform of a JSON object.
            try {
                URL url = new URL(serverIP);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                Log.i("JSON", jsonMessage.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonMessage.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //check for server updates
        checkForUpdate();

        //adding successful sent message to local chat list
        chatLog.add(localMessage);
        lastupdate = localMessage.timeStamp;
        int latestChatPosition = chatLog.size() - 1;
        chatAdapter.notifyItemInserted(latestChatPosition);
    }
}