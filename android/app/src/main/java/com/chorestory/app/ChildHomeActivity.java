package com.chorestory.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.chorestory.R;
import com.chorestory.adapter.ChildHomeAdapter;
import com.chorestory.fragment.ChildClanFragment;
import com.chorestory.fragment.ChildFriendsFragment;
import com.chorestory.fragment.ChildProfileFragment;
import com.chorestory.fragment.ChildQuestsFragment;

import java.util.Arrays;
import java.util.List;

public class ChildHomeActivity extends ChoreStoryActivity {

    private final String CLAN = "Clan";
    private final String FRIENDS = "Friends";
    private final String QUESTS = "Quests";
    private final String PROFILE = "Profile";

    private final int[] tabSelectedIcons = {
            R.drawable.castle_color,
            R.drawable.friends_color,
            R.drawable.sword_color,
            R.drawable.knight_color
    };

    private final int[] tabsUnselectedIcons = {
            R.drawable.castle_bw,
            R.drawable.friends_bw,
            R.drawable.sword_bw,
            R.drawable.knight_bw
    };

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ChildHomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_child_home);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        List<Fragment> fragmentList = Arrays.asList(new ChildClanFragment(),
                new ChildFriendsFragment(),
                new ChildQuestsFragment(),
                new ChildProfileFragment());

        List<String> fragmentTitleList = Arrays.asList(CLAN, FRIENDS, QUESTS, PROFILE);

        adapter = new ChildHomeAdapter(getSupportFragmentManager(),
                fragmentList,
                fragmentTitleList);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set default icons
        tabLayout.getTabAt(0).setIcon(tabSelectedIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabsUnselectedIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabsUnselectedIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabsUnselectedIcons[3]);
        // Update icon on tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(tabSelectedIcons[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(tabsUnselectedIcons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
