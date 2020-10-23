package com.example.community_link;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chatRecycleViewAdapter extends RecyclerView.Adapter<chatViewHolder> {
    public List<chatMessage> chatLog;
    public String userID;

    public chatRecycleViewAdapter(List<chatMessage> chatLog, String userID) {
        this.chatLog = chatLog;
        this.userID = userID;
    }


    // Create new views (invoked by the layout manager)
    //viewTypes: 0=Receiving; else=Sending
    @Override
    @NonNull
    public chatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chat_message_ver2, parent, false);

        return new chatViewHolder(v);
    }

    // Set contents of the Views
    @Override
    public void onBindViewHolder(chatViewHolder holder, int position) {
        chatMessage c = this.chatLog.get(position);
        //keep only one of 2 views and set contents to display
        if(c.sender.equals(userID)){
            holder.sentMessage.setText(c.content);
            //holder.sentTime.setText(c.timeStamp);
            holder.sent.setVisibility(LinearLayout.VISIBLE);
            holder.received.setVisibility(LinearLayout.GONE);
        }else{
            holder.receivedMessage.setText(c.content);
            //holder.receivedTime.setText(c.timeStamp);
            //holder.receivedName.setText(c.sender);
            holder.received.setVisibility(LinearLayout.VISIBLE);
            holder.sent.setVisibility(LinearLayout.GONE);
        }
    }

    // Return the size of current chat list
    @Override
    public int getItemCount() {
        return chatLog.size();
    }
}