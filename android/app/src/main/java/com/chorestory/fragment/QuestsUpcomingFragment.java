package com.chorestory.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chorestory.R;
import com.chorestory.adapter.QuestRecyclerViewAdapter;
import com.chorestory.app.ChoreStoryActivity;
import com.chorestory.helpers.QuestCompletion;
import com.chorestory.model.QuestParcelable;
import com.chorestory.model.QuestRecyclerViewItem;

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

        List<QuestRecyclerViewItem> overdueQuests = getOverdueQuests(QuestRecyclerViewItem.getData(questParcelables));
        overdueQuestsAdapter = new QuestRecyclerViewAdapter(overdueQuests, (ChoreStoryActivity) getActivity());
        overdueQuestsRecyclerView.setAdapter(overdueQuestsAdapter);

        // Upcoming Quests
        upcomingQuestsRecyclerView = view.findViewById(R.id.upcoming_quests_recycler_view);
        upcomingQuestsLayoutManager = new LinearLayoutManager(getActivity());
        upcomingQuestsRecyclerView.setLayoutManager(upcomingQuestsLayoutManager);

        List<QuestRecyclerViewItem> upcomingQuests = getUpcomingQuests(QuestRecyclerViewItem.getData(questParcelables));
        upcomingQuestsAdapter = new QuestRecyclerViewAdapter(upcomingQuests, (ChoreStoryActivity) getActivity());
        upcomingQuestsRecyclerView.setAdapter(upcomingQuestsAdapter);

        return view;
    }

    private List<QuestRecyclerViewItem> getOverdueQuests(List<QuestRecyclerViewItem> allQuests) {
        List<QuestRecyclerViewItem> overdueQuests = new ArrayList<>();
        for (QuestRecyclerViewItem quest : allQuests) {
            if (quest.getStatus() == QuestCompletion.OVERDUE) {
                overdueQuests.add(quest);
            }
        }
        return overdueQuests;
    }

    private List<QuestRecyclerViewItem> getUpcomingQuests(List<QuestRecyclerViewItem> allQuests) {
        List<QuestRecyclerViewItem> upcomingQuests = new ArrayList<>();
        for (QuestRecyclerViewItem quest : allQuests) {
            if (quest.getStatus() == QuestCompletion.UPCOMING) {
                upcomingQuests.add(quest);
            }
        }
        return upcomingQuests;
    }
}

