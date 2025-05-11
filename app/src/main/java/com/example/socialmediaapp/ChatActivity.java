package com.example.socialmediaapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText input;
    Button send;
    RecyclerView rv;
    MessageAdapter adapter;
    ArrayList<Message> list;

    String currentUserId;
    String receiverId;
    DatabaseReference ref;

    private String getChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        input = findViewById(R.id.messageInput);
        send = findViewById(R.id.sendButton);
        rv = findViewById(R.id.chatRecyclerView);

        list = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getUid();
        receiverId = getIntent().getStringExtra("receiverId");

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this, list, currentUserId);
        rv.setAdapter(adapter);

        String chatId = getChatId(currentUserId, receiverId);
        ref = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Message m = snap.getValue(Message.class);
                    list.add(m);
                }
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = input.getText().toString().trim();
                if (msg.isEmpty()) return;

                String key = ref.push().getKey();
                Message m = new Message(currentUserId, msg, System.currentTimeMillis());
                ref.child(key).setValue(m);
                input.setText("");
            }
        });
    }
}
