package com.chorestory.app;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chorestory.R;
import com.chorestory.adapter.HomeAdapter;
import com.chorestory.fragment.ParentCreateFragment;
import com.chorestory.fragment.ParentClanFragment;
import com.chorestory.fragment.ParentProfileFragment;
import com.chorestory.fragment.ParentQuestsFragment;

import java.util.Arrays;
import java.util.List;

public class ParentHomeActivity extends ChoreStoryActivity {

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

    private HomeAdapter homeAdapter;

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
                new ParentCreateFragment(),
                new ParentQuestsFragment(),
                new ParentProfileFragment());

        List<String> fragmentTitleList = Arrays.asList(getString(R.string.clan),
                getString(R.string.create),
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
