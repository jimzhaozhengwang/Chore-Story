package com.chorestory.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.design.widget.TabLayout;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;
import com.chorestory.adapter.ParentQuestsAdapter;

import com.chorestory.R;

public class ParentQuestsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_quests, container, false);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicatorHeight(0);

        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        ParentQuestsAdapter adapter = new ParentQuestsAdapter(getChildFragmentManager());
        adapter.addFragment(new ParentQuestsTodoFragment(), "Upcoming");
        adapter.addFragment(new ParentQuestsPendingFragment(), "Pending");
        adapter.addFragment(new ParentQuestsCompletedFragment(), "Completed");
        viewPager.setAdapter(adapter);
    }
}

