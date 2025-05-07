package com.example.socialmediaapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FeedFragment extends Fragment {

    RecyclerView rv;
    View v;
    RVAdapter adapter;
    private void init(){
        rv=v.findViewById(R.id.rvid);
        Query query= FirebaseDatabase.getInstance().getReference().child("posts");
        FirebaseRecyclerOptions<PostsData> opt= new FirebaseRecyclerOptions
                .Builder<PostsData>().
                setQuery(query, PostsData.class).
                build();
        adapter=new RVAdapter(opt,requireContext());
        rv.setAdapter(adapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_feed, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

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