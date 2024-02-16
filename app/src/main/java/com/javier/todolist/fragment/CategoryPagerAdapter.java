package com.javier.todolist.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CategoryPagerAdapter extends FragmentStateAdapter {

    public CategoryPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new PendingFragment();
            case 1: return new DoneFragment();
            case 2: return new OverDueFragment();
            default: return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
