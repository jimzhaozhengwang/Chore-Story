package com.chorestory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;

import com.chorestory.adapter.ParentQuestsAdapter;

import java.util.Arrays;
import java.util.List;

import com.chorestory.R;

public class ParentQuestsFragment extends Fragment {

    private final String UPCOMING = "Upcoming";
    private final String PENDING = "Pending";
    private final String COMPLETED = "Completed";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ParentQuestsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_quests, container, false);

        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager();
        tabLayout = view.findViewById(R.id.result_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(0);

        return view;
    }

    private void setupViewPager() {
        List<Fragment> fragmentList = Arrays.asList(new ParentQuestsUpcomingFragment(),
                new ParentQuestsPendingFragment(),
                new ParentQuestsCompletedFragment());
        List<String> fragmentTitleList = Arrays.asList(UPCOMING, PENDING, COMPLETED);
        adapter = new ParentQuestsAdapter(getChildFragmentManager(), fragmentList, fragmentTitleList);
        viewPager.setAdapter(adapter);
    }
}
