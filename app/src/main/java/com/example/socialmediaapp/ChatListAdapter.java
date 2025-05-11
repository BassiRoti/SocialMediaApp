package com.example.socialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChatListAdapter extends FirebaseRecyclerAdapter<UserData, ChatListAdapter.ViewHolder> {

    Context c;
    String currentUid;

    public ChatListAdapter(@NonNull FirebaseRecyclerOptions<UserData> options, Context context, String currentUid) {
        super(options);
        this.c = context;
        this.currentUid = currentUid;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserData model) {
        String uid = getRef(position).getKey();
        Log.d("ChatSelection", "ChatListAdapter: Processing item. currentLoggedInUser=" + currentUid + ", userInThisListItem=" + uid + ", usernameFromModel=" + model.getUsername());
        if (!uid.equals(currentUid)) {
            holder.name.setText(model.getUsername());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c, ChatActivity.class);
                    i.putExtra("receiverId", uid);
                    c.startActivity(i);
                }
            });
        }
        else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_user_row, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvusername);
        }
    }
}