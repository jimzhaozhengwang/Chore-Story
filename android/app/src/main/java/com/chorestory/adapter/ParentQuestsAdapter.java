package com.chorestory.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ParentQuestsAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> fragmentTitleList;

    public ParentQuestsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentList = new ArrayList<>();
        fragmentTitleList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
