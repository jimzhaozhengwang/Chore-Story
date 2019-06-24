package com.chorestory.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ParentHomeAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> fragmentTitleList;

    public ParentHomeAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentTitleList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String fragmentTitle) {
        fragmentList.add(fragment);
        fragmentTitleList.add(fragmentTitle);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
