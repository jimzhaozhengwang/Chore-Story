package com.chorestory.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.chorestory.R;
import com.chorestory.adapter.ParentHomeAdapter;
import com.chorestory.fragment.ParentAddFragment;
import com.chorestory.fragment.ParentClanFragment;
import com.chorestory.fragment.ParentProfileFragment;
import com.chorestory.fragment.ParentQuestsFragment;

import java.util.Arrays;
import java.util.List;

public class ParentHomeActivity extends ChoreStoryActivity {

    private final String CLAN = "Clan";
    private final String ADD = "Add";
    private final String QUESTS = "Quests";
    private final String PROFILE = "Profile";
    private final int[] tabSelectedIcons = {
            R.drawable.castle_color,
            R.drawable.plus_color,
            R.drawable.sword_color,
            R.drawable.knight_color
    };
    private final int[] tabsUnselectedIcons = {
            R.drawable.castle_bw,
            R.drawable.plus_bw,
            R.drawable.sword_bw,
            R.drawable.knight_bw
    };

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ParentHomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_home);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        final String CLAN_NAME = getResources().getString(R.string.clan_name);

        Bundle bundle = new Bundle();
        bundle.putString(CLAN_NAME, getIntent().getStringExtra(CLAN_NAME));
        ParentClanFragment parentClanFragment = new ParentClanFragment();
        parentClanFragment.setArguments(bundle);

        List<Fragment> fragmentList = Arrays.asList(parentClanFragment,
                new ParentAddFragment(),
                new ParentQuestsFragment(),
                new ParentProfileFragment());

        List<String> fragmentTitleList = Arrays.asList(CLAN, ADD, QUESTS, PROFILE);

        adapter = new ParentHomeAdapter(getSupportFragmentManager(),
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

