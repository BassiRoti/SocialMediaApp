package com.example.socialmediaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainConfigurator extends AppCompatActivity {
    ViewPager2 vp;
    TabLayout tb;
    PagerAdapter adapter;
    Uri image;

    ActivityResultLauncher<Intent> launcher;

    private void init(){
        vp=findViewById(R.id.viewpagerid);
        tb=findViewById(R.id.tablayoutid);
        adapter=new PagerAdapter(this);
        vp.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_configurator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        new TabLayoutMediator(tb, vp, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.feed_icon);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.chat_icon);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.add_icon);

                        break;
                    case 3:
                        tab.setIcon(R.drawable.profile_icon);
                        break;
                    default:
                        tab.setIcon(R.drawable.feed_icon);
                        break;
                }
            }
        }).attach();
//
//        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
//            if(result.getData()!=null && result.getResultCode()==RESULT_OK){
//                image=result.getData().getData();
//            }
//        });
//
//        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position =tab.getPosition();
//                if(position==2){
//                    Intent i=new Intent(Intent.ACTION_PICK);
//                    i.setType("image/*");
//                    launcher.launch(i);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }
}