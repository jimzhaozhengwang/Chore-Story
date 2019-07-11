package com.chorestory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chorestory.R;
import com.chorestory.adapter.QuestRecyclerViewAdapter;
import com.chorestory.app.ChoreStoryActivity;
import com.chorestory.model.QuestRecyclerViewItem;
import com.chorestory.module.QuestCompletion;

public class ParentQuestsPendingFragment extends Fragment {

    private RecyclerView pendingQuestsRecyclerView;
    private RecyclerView.Adapter pendingQuestsAdapter;
    private RecyclerView.LayoutManager pendingQuestsLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_parent_quests_pending, container, false);

        // Pending Quests
        pendingQuestsRecyclerView = view.findViewById(R.id.pending_quests_recycler_view);
        pendingQuestsLayoutManager = new LinearLayoutManager(getActivity());
        pendingQuestsRecyclerView.setLayoutManager(pendingQuestsLayoutManager);

        pendingQuestsAdapter = new QuestRecyclerViewAdapter(QuestRecyclerViewItem.getData(QuestCompletion.PENDING), (ChoreStoryActivity) getActivity());
        pendingQuestsRecyclerView.setAdapter(pendingQuestsAdapter);

        return view;
    }
}
