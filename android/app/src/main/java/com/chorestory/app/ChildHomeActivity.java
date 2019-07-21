package com.chorestory.app;

import android.os.Bundle;

import com.chorestory.services.NotificationService;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chorestory.R;
import com.chorestory.adapter.HomeAdapter;
import com.chorestory.fragment.ChildClanFragment;
import com.chorestory.fragment.ChildFriendsFragment;
import com.chorestory.fragment.ChildProfileFragment;
import com.chorestory.fragment.ChildQuestsFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;

public class ChildHomeActivity extends ChoreStoryActivity {

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

    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_child_home);

        // Send registration id to server
        NotificationService notificationService = new NotificationService();
        String token = FirebaseInstanceId.getInstance().getToken();
        notificationService.sendRegistrationToServer(token, getApplicationContext());

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        List<Fragment> fragmentList = Arrays.asList(new ChildClanFragment(),
                new ChildFriendsFragment(),
                new ChildQuestsFragment(),
                new ChildProfileFragment());

        List<String> fragmentTitleList = Arrays.asList(getString(R.string.clan),
                getString(R.string.friends),
                getString(R.string.quests),
                getString(R.string.profile));

        homeAdapter = new HomeAdapter(getSupportFragmentManager(),
                fragmentList,
                fragmentTitleList);

        viewPager.setAdapter(homeAdapter);
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

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
