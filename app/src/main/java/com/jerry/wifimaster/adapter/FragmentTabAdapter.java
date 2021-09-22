package com.jerry.wifimaster.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import javax.inject.Inject;

public class FragmentTabAdapter extends FragmentPagerAdapter {

    protected List<Fragment> mFragments;
    
    public FragmentTabAdapter( FragmentManager fm, List<Fragment> fragments) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
