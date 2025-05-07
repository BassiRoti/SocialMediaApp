package com.example.socialmediaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MultiImagePagerAdapter extends RecyclerView.Adapter<MultiImagePagerAdapter.ImageViewHolder> {

    Context context;
    List<String> imagelist;
    public MultiImagePagerAdapter(Context c, List<String> ImageList){
        context=c;
        imagelist=ImageList;
    }

    @NonNull
    @Override
    public MultiImagePagerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.post_images,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiImagePagerAdapter.ImageViewHolder holder, int position) {
        String simgle_img=imagelist.get(position);

        Glide.with(context)
                .load(simgle_img)
                .error(R.drawable.error_icon)
                .into(holder.imgv);

    }

    @Override
    public int getItemCount() {
//        Log.d("listsize", "Image list size: " + imagelist.size());
        return imagelist.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgv;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv=itemView.findViewById(R.id.postImage);
        }
    }
}
