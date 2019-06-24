package com.chorestory.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.chorestory.R;
import com.chorestory.adapter.ParentHomeAdapter;
import com.chorestory.fragment.ParentAddFragment;
import com.chorestory.fragment.ParentClanFragment;
import com.chorestory.fragment.ParentProfileFragment;
import com.chorestory.fragment.ParentQuestsFragment;

public class ParentHomeActivity extends ChoreStoryActivity {

    private final String CLAN = "clan";
    private final String ADD = "Add";
    private final String QUEST = "Quests";
    private final String PROFILE = "Profile";

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

        adapter = new ParentHomeAdapter(getSupportFragmentManager());

        final String CLAN_NAME = getResources().getString(R.string.clan_name);

        Bundle bundle = new Bundle();
        bundle.putString(CLAN_NAME, getIntent().getStringExtra(CLAN_NAME));
        ParentClanFragment parentClanFragment = new ParentClanFragment();
        parentClanFragment.setArguments(bundle);
        adapter.addFragment(parentClanFragment, CLAN);

        adapter.addFragment(new ParentAddFragment(), ADD);
        adapter.addFragment(new ParentQuestsFragment(), QUEST);
        adapter.addFragment(new ParentProfileFragment(), PROFILE);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // TODO: change icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher_foreground);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_foreground);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher_foreground);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_launcher_foreground);
    }
}
