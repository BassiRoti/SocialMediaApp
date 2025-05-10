package com.example.socialmediaapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    ImageView userimg;
    TextView username, bio;
    RecyclerView profilerv;
    View v;
    SharedPreferences sp;

    Button logoutbtn;
    Button editbtn;
    SharedPreferences.Editor editor;
    ProfileRVAdapter adapter;



    private void init(){
        userimg=v.findViewById(R.id.idLogo);
        username=v.findViewById(R.id.profilename);
        profilerv=v.findViewById(R.id.profilerv);
        logoutbtn=v.findViewById(R.id.logoutbtn);
        editbtn=v.findViewById(R.id.editpfbtn);
        bio=v.findViewById(R.id.bio);
        sp= getContext().getSharedPreferences("authentication_data",MODE_PRIVATE);
        editor=sp.edit();
        username.setText(sp.getString("user_name",""));
        bio.setText(sp.getString("user_bio",""));
        String id=FirebaseAuth.getInstance().getUid();
        Query query = FirebaseDatabase.getInstance()
                .getReference("posts")
                .orderByChild("userID")
                .equalTo(id);
        FirebaseRecyclerOptions<PostsData> opt= new FirebaseRecyclerOptions
                .Builder<PostsData>().
                setQuery(query, PostsData.class).
                build();
        adapter=new ProfileRVAdapter(opt,requireContext(),id);
        profilerv.setAdapter(adapter);

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
                editor.putString("Username","");
                editor.putString("Bio","");
                editor.putString("userID","");
                editor.putString("Image","");
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getContext(),MainActivity.class);
                startActivity(i);
            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View vv = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialogue, null, false);
                builder.setView(vv);

                Button dialoguesavebtn = vv.findViewById(R.id.dialoguesavebtn);
                EditText Dialoguebio = vv.findViewById(R.id.dialoguebio);

                AlertDialog dialog = builder.create();
                dialog.show();

                dialoguesavebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updatedBio = Dialoguebio.getText().toString().trim();
                        if (!updatedBio.isEmpty()) {
                            FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(sp.getString("user_id", ""))
                                    .child("Bio")
                                    .setValue(updatedBio)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            bio.setText(updatedBio);
                                            editor.putString("user_bio", updatedBio).apply();
                                            dialog.dismiss();
                                            Toast.makeText(requireContext(), "Bio updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(requireContext(), "Failed to update bio", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(requireContext(), "Bio cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}