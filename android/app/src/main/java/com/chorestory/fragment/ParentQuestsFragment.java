package com.chorestory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.adapter.QuestsAdapter;
import com.chorestory.app.App;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.model.QuestParcelable;
import com.chorestory.templates.GetQuestsResponse;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentQuestsFragment extends ChoreStoryFragment {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    private long SECONDS_IN_WEEK = 604800;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ArrayList<QuestParcelable> questParcelables;

    private QuestsAdapter questsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_quests, container, false);

        App.getAppComponent().inject(this);

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        return view;
    }

    private void setupViewPager() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.all_quests), questParcelables);

        QuestsUpcomingFragment questsUpcomingFragment = new QuestsUpcomingFragment();
        questsUpcomingFragment.setArguments(bundle);
        QuestsPendingFragment questsPendingFragment = new QuestsPendingFragment();
        questsPendingFragment.setArguments(bundle);
        QuestsCompletedFragment questsCompletedFragment = new QuestsCompletedFragment();
        questsCompletedFragment.setArguments(bundle);

        List<Fragment> fragmentList = Arrays.asList(questsUpcomingFragment,
                questsPendingFragment,
                questsCompletedFragment);
        List<String> fragmentTitleList = Arrays.asList(getString(R.string.upcoming),
                getString(R.string.pending),
                getString(R.string.completed));
        questsAdapter = new QuestsAdapter(getChildFragmentManager(), fragmentList, fragmentTitleList);
        viewPager.setAdapter(questsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getQuests();
    }

    private void getQuests() {
        String token = tokenHandler.getToken(getContext());
        if (token != null) {
            if (tokenHandler.isParentToken(token)) {
                long currentTime = System.currentTimeMillis() / 1000;
                long start = currentTime - SECONDS_IN_WEEK;
                long lookahead = SECONDS_IN_WEEK * 2;
                Call<GetQuestsResponse> getQuestsQuery = retrofitInterface.get_all_quests(token, start + ".0", lookahead + ".0");
                getQuestsQuery.enqueue(new Callback<GetQuestsResponse>() {
                    @Override
                    public void onResponse(Call<GetQuestsResponse> call, Response<GetQuestsResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                            List<GetQuestsResponse.Quest> allQuests = response.body().getQuests();
                            questParcelables = new ArrayList<>();
                            for (int i = 0; i < allQuests.size(); i++) {
                                questParcelables.add(new QuestParcelable(allQuests.get(i)));
                            }
                            setupViewPager();
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetQuestsResponse> call, Throwable t) {
                        Toaster.showToast(getContext(), "Internal error occurred.");
                        deleteTokenNavigateMain(getContext());
                    }
                });
            } else {
                deleteTokenNavigateMain(getContext());
            }
        }
    }
}
