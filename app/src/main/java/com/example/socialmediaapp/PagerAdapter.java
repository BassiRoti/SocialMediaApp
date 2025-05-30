package com.example.socialmediaapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FeedFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new AddPostFrag();
            case 3:
                return new ProfileFragment();
            default:
                return new FeedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }


}
