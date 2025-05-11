package com.example.socialmediaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context c;
    ArrayList<Message> list;
    String currentUserId;

    public MessageAdapter(Context context, ArrayList<Message> list, String currentUserId) {
        this.c = context;
        this.list = list;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender().equals(currentUserId))
            return 1; // sent
        else
            return 0; // received
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1)
            v = LayoutInflater.from(c).inflate(R.layout.item_message_sent, parent, false);
        else
            v = LayoutInflater.from(c).inflate(R.layout.item_message_received, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message m = list.get(position);
        holder.msg.setText(m.getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.messageText);
        }
    }
}