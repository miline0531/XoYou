package com.redrover.xoyou.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import com.redrover.xoyou.common.SkyLog;

public class SectionPageAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> mFragments;

    public SectionPageAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList list) {
        super(fragmentActivity);
        this.mFragments = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        SkyLog.d("position :: " + position);
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}