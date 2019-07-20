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

public class QuestsCompletedFragment extends ChoreStoryFragment {

    private RecyclerView completedQuestsRecyclerView;
    private RecyclerView.Adapter completedQuestsAdapter;
    private RecyclerView.LayoutManager completedQuestsLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests_completed, container, false);

        Bundle bundle = this.getArguments();
        ArrayList<QuestParcelable> questParcelables = bundle.getParcelableArrayList(getString(R.string.all_quests));

        // Completed Quests
        completedQuestsRecyclerView = view.findViewById(R.id.completed_quests_recycler_view);
        completedQuestsLayoutManager = new LinearLayoutManager(getActivity());
        completedQuestsRecyclerView.setLayoutManager(completedQuestsLayoutManager);

        completedQuestsAdapter = new QuestRecyclerViewAdapter(QuestRecyclerViewItem.getData(questParcelables), (ChoreStoryActivity) getActivity());
        completedQuestsRecyclerView.setAdapter(completedQuestsAdapter);

        return view;
    }
}
