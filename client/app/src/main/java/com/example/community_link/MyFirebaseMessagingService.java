package com.example.community_link;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d("chat:PushNotification", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("chat:PushNotification", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("chat:PushNotification", "Message data payload: " + remoteMessage.getData());

            String sender = remoteMessage.getData().get("sender");
            String recipient = remoteMessage.getData().get("recipient");
            String timestamp = remoteMessage.getData().get("timestamp");
            String content = remoteMessage.getData().get("content");

            if(sender == null || recipient == null || timestamp== null || content == null){
                System.out.println("chat:PushNotification Message received is not complete, ignoring the message.");
                return;
            }

            ChatMessage newMessage =  new ChatMessage(sender, recipient, timestamp, content);
            Intent intent = new Intent("pushNdata");
            intent.putExtra("pushNdata", newMessage.toJson().toString());
            broadcaster.sendBroadcast(intent);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("chat:PushNotification", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Intent intent = new Intent("pushNdata");
            intent.putExtra("pushNdata", remoteMessage.getNotification().getBody());
            broadcaster.sendBroadcast(intent);
        }
    }
}
