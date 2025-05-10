package com.example.socialmediaapp;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;


public class AddPostFrag extends Fragment {
    View v;
    View v2;
    Uri image;
    ActivityResultLauncher<Intent> launcher;
    ImageView imgview;
    EditText caption;
    Button btnaddpost;

    private void init(){
//        v2=LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialogue,null,false);
        imgview=v.findViewById(R.id.dialogueimage);
        caption=v.findViewById(R.id.dialoguecaption);
        btnaddpost=v.findViewById(R.id.btnaddpost);
//        FirebaseDatabase.getInstance().getReference("posts").child()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_add_post, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
//        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
//        builder.setView(v2);
//        builder.show();

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getData()!=null && result.getResultCode()==RESULT_OK){
                image=result.getData().getData();
                imgview.setImageURI(image);
            }
        });

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                launcher.launch(i);
            }
        });

        btnaddpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("userID","1");//for now, baad mai use shared ref for logged in user
                map.put("caption",caption.getText().toString().trim());
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                map.put("timespam",ts+"ago");
//                HashMap<String,Object> map2=new HashMap<>();
//                map2.put("0",image.toString()); // hardcoded image count
//                map.put("imageUrls",map2);

               DatabaseReference ref= FirebaseDatabase.getInstance().getReference("posts");
               String key=ref.push().getKey();

               FirebaseDatabase.getInstance().getReference("posts").child(key).setValue(map);


            }
        });
    }
}