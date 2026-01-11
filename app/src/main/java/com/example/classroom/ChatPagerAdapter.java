package com.example.classroom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ChatPagerAdapter extends FragmentStateAdapter {

    public ChatPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new AiTutorFragment();
        } else {
            return new UserChatFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
