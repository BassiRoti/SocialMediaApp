package com.example.socialmediaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RVAdapter extends FirebaseRecyclerAdapter<PostsData, RVAdapter.ViewHolder> {
    Context c;
    public RVAdapter(@NonNull FirebaseRecyclerOptions<PostsData> options, Context context) {
        super(options);
        c=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostsData model) {
        String tempid= model.getUserID();
        FirebaseDatabase.getInstance().getReference("users").child(tempid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   String tempusername=snapshot.child("Username").getValue(String.class);
                   holder.username.setText(tempusername);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(c, "an error occured while fetching username", Toast.LENGTH_SHORT).show();
            }
        });
        holder.caption.setText(model.getCaption());
        holder.timespam.setText(model.getTimespam());

        MultiImagePagerAdapter adapter=new MultiImagePagerAdapter(c,model.getImageUrls());
        holder.vp2.setAdapter(adapter);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View v=LayoutInflater.from(c).inflate(R.layout.custom_feed_view,parent,false);
     return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView username, caption, timespam;
        ViewPager2 vp2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.tvuserimg);
            username=itemView.findViewById(R.id.usernameText);
            caption=itemView.findViewById(R.id.tvcaption);
            timespam=itemView.findViewById(R.id.tvtimespam);
            vp2=itemView.findViewById(R.id.vpimage);

        }
    }
}