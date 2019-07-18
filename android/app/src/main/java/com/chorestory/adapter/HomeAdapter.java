package com.chorestory.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class HomeAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> fragmentTitleList;

    public HomeAdapter(FragmentManager fragmentManager,
                       List<Fragment> fragmentList,
                       List<String> fragmentTitleList) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.fragmentTitleList = fragmentTitleList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
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
