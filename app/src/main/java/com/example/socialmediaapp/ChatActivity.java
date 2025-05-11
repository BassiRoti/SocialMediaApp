package com.example.socialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    RecyclerView.LayoutManager manager;
    MessageAdapter adapter;
    ArrayList<Message> list;

    String currentUserId;
    String receiverId;
    DatabaseReference ref;
    ImageButton back;
    private void init(){
        input = findViewById(R.id.messageInput);
        send = findViewById(R.id.sendButton);
        rv = findViewById(R.id.chatRecyclerView);
        list = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getUid();
        Intent i=getIntent();
        receiverId = i.getStringExtra("receiverId");
        manager=new LinearLayoutManager(this);
        adapter=new MessageAdapter(this,list,currentUserId);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        back=findViewById(R.id.backButton);
    }

    private String getChatId(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "_" + uid2;
        } else {
            return uid2 + "_" + uid1;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

//        Log.d("ChatCheck", "Current UID: " + currentUserId);
//        Log.d("ChatCheck", "Receiver UID: " + receiverId);
//        Log.d("ChatCheck", "Chat ID: " + getChatId(currentUserId, receiverId));


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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ChatActivity.this, MainConfigurator.class);
                startActivity(i);
                finish();
            }
        });
    }
}
