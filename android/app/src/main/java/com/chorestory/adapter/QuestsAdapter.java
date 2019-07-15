package com.chorestory.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class QuestsAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> fragmentTitleList;

    public QuestsAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList, List<String> fragmentTitleList) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.fragmentTitleList = fragmentTitleList;
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
