package com.example.community_link;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class chatViewHolder extends RecyclerView.ViewHolder {
    //UI representation for received message displays
    LinearLayout received;
    //TextView receivedName;
    //TextView receivedTime;
    TextView receivedMessage;

    //UI representation for sent message displays
    LinearLayout sent;
    //TextView sentTime;
    TextView sentMessage;

    public chatViewHolder(View v) {
        super(v);

        //assigning the displays to appropriate elements
        received = v.findViewById(R.id.chat_left_msg_layout);
        receivedMessage = v.findViewById(R.id.chat_left_msg_text_view);

        sent = v.findViewById(R.id.chat_right_msg_layout);
        sentMessage = v.findViewById(R.id.chat_right_msg_text_view);
    }
}

