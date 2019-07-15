package com.chorestory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;

import com.chorestory.adapter.QuestsAdapter;

import java.util.Arrays;
import java.util.List;

import com.chorestory.R;

public class ChildQuestsFragment extends ChoreStoryFragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private QuestsAdapter questsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_quests, container, false);

        viewPager = view.findViewById(R.id.view_pager);
        setupViewPager();
        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(0);

        return view;
    }

    private void setupViewPager() {
        List<Fragment> fragmentList = Arrays.asList(new QuestsUpcomingFragment(),
                new QuestsPendingFragment(),
                new QuestsCompletedFragment());
        List<String> fragmentTitleList = Arrays.asList(getString(R.string.upcoming),
                getString(R.string.pending),
                getString(R.string.completed));
        questsAdapter = new QuestsAdapter(getChildFragmentManager(), fragmentList, fragmentTitleList);
        viewPager.setAdapter(questsAdapter);
    }
}
