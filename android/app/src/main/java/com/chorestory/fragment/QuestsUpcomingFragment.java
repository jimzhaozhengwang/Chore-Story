package com.chorestory.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chorestory.R;
import com.chorestory.adapter.QuestRecyclerViewAdapter;
import com.chorestory.app.ChoreStoryActivity;
import com.chorestory.helpers.QuestCompletion;
import com.chorestory.model.QuestParcelable;
import com.chorestory.model.QuestRecyclerViewItem;
import com.chorestory.templates.GetQuestsResponse;

import java.util.ArrayList;
import java.util.List;

public class QuestsUpcomingFragment extends ChoreStoryFragment {

    private RecyclerView overdueQuestsRecyclerView;
    private RecyclerView.Adapter overdueQuestsAdapter;
    private RecyclerView.LayoutManager overdueQuestsLayoutManager;
    private RecyclerView upcomingQuestsRecyclerView;
    private RecyclerView.Adapter upcomingQuestsAdapter;
    private RecyclerView.LayoutManager upcomingQuestsLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests_upcoming, container, false);

        Bundle bundle = this.getArguments();
        ArrayList<QuestParcelable> questParcelables = bundle.getParcelableArrayList("all_quests");

        // Overdue Quests
        overdueQuestsRecyclerView = view.findViewById(R.id.overdue_quests_recycler_view);
        overdueQuestsLayoutManager = new LinearLayoutManager(getActivity());
        overdueQuestsRecyclerView.setLayoutManager(overdueQuestsLayoutManager);

        overdueQuestsAdapter = new QuestRecyclerViewAdapter(QuestRecyclerViewItem.getData(questParcelables), (ChoreStoryActivity) getActivity());
        overdueQuestsRecyclerView.setAdapter(overdueQuestsAdapter);

        // Upcoming Quests
        upcomingQuestsRecyclerView = view.findViewById(R.id.upcoming_quests_recycler_view);
        upcomingQuestsLayoutManager = new LinearLayoutManager(getActivity());
        upcomingQuestsRecyclerView.setLayoutManager(upcomingQuestsLayoutManager);

        upcomingQuestsAdapter = new QuestRecyclerViewAdapter(QuestRecyclerViewItem.getData(questParcelables), (ChoreStoryActivity) getActivity());
        upcomingQuestsRecyclerView.setAdapter(upcomingQuestsAdapter);

        return view;
    }
}
