package com.chorestory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chorestory.R;
import com.chorestory.adapter.QuestRecyclerViewAdapter;
import com.chorestory.app.ChoreStoryActivity;
import com.chorestory.model.QuestParcelable;
import com.chorestory.model.QuestRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class QuestsCompletedFragment extends ChoreStoryFragment {

    private RecyclerView completedQuestsRecyclerView;
    private RecyclerView.Adapter completedQuestsAdapter;
    private RecyclerView.LayoutManager completedQuestsLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests_completed, container, false);

        Bundle bundle = this.getArguments();
        ArrayList<QuestParcelable> questParcelables = bundle.getParcelableArrayList("all_quests");

        // Completed Quests
        completedQuestsRecyclerView = view.findViewById(R.id.completed_quests_recycler_view);
        completedQuestsLayoutManager = new LinearLayoutManager(getActivity());
        completedQuestsRecyclerView.setLayoutManager(completedQuestsLayoutManager);

        List<QuestRecyclerViewItem> completedQuests = getCompletedQuests(QuestRecyclerViewItem.getData(questParcelables));
        completedQuestsAdapter = new QuestRecyclerViewAdapter(completedQuests, (ChoreStoryActivity) getActivity());
        completedQuestsRecyclerView.setAdapter(completedQuestsAdapter);

        return view;
    }


    private List<QuestRecyclerViewItem> getCompletedQuests(List<QuestRecyclerViewItem> allQuests) {
        List<QuestRecyclerViewItem> completedQuests = new ArrayList<>();
        for (QuestRecyclerViewItem quest : allQuests) {
            if (quest.getStatus() == QuestCompletion.COMPLETED) {
                completedQuests.add(quest);
            }
        }
        return completedQuests;
    }
}
