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

public class QuestsPendingFragment extends ChoreStoryFragment {

    private RecyclerView pendingQuestsRecyclerView;
    private RecyclerView.Adapter pendingQuestsAdapter;
    private RecyclerView.LayoutManager pendingQuestsLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests_pending, container, false);

        Bundle bundle = this.getArguments();
        ArrayList<QuestParcelable> questParcelables = bundle.getParcelableArrayList("all_quests");

        // Pending Quests
        pendingQuestsRecyclerView = view.findViewById(R.id.pending_quests_recycler_view);
        pendingQuestsLayoutManager = new LinearLayoutManager(getActivity());
        pendingQuestsRecyclerView.setLayoutManager(pendingQuestsLayoutManager);

        List<QuestRecyclerViewItem> pendingQuests = getPendingQuests(QuestRecyclerViewItem.getData(questParcelables));
        pendingQuestsAdapter = new QuestRecyclerViewAdapter(pendingQuests, (ChoreStoryActivity) getActivity());
        pendingQuestsRecyclerView.setAdapter(pendingQuestsAdapter);

        return view;
    }

    private List<QuestRecyclerViewItem> getPendingQuests(List<QuestRecyclerViewItem> allQuests) {
        List<QuestRecyclerViewItem> pendingQuests = new ArrayList<>();
        for (QuestRecyclerViewItem quest : allQuests) {
            if (quest.getStatus() == QuestCompletion.PENDING) {
                pendingQuests.add(quest);
            }
        }
        return pendingQuests;
    }
}
