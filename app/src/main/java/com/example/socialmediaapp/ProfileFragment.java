package com.example.socialmediaapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    ImageView userimg;
    TextView username, bio;
    RecyclerView profilerv;
    View v;
    SharedPreferences sp;

    Button logoutbtn;

    private void init(){
        userimg=v.findViewById(R.id.idLogo);
        username=v.findViewById(R.id.profilename);
        profilerv=v.findViewById(R.id.profilerv);
        logoutbtn=v.findViewById(R.id.logoutbtn);
        bio=v.findViewById(R.id.bio);
        sp= getContext().getSharedPreferences("authentication_data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        username.setText(sp.getString("user_name",""));
        bio.setText(sp.getString("user_bio",""));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
//        FirebaseDatabase.getInstance().getReference("users").child("user1").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userimg.setImageURI(Uri.parse());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getContext(),MainActivity.class);
                startActivity(i);
            }
        });


    }
}