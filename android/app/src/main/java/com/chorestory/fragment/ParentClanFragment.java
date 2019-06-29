package com.chorestory.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.adapter.ChildRecyclerViewAdapter;
import com.chorestory.adapter.ParentRecyclerViewAdapter;
import com.chorestory.model.ChildRecyclerViewItem;
import com.chorestory.model.ParentRecyclerViewItem;

public class ParentClanFragment extends Fragment {

    private final String CLAN = " Clan";
    private TextView clanNameTextView;
    private RecyclerView parentRecyclerView;
    private RecyclerView.Adapter parentAdapter;
    private RecyclerView.LayoutManager parentLayoutManager;
    private RecyclerView childRecyclerView;
    private RecyclerView.Adapter childAdapter;
    private RecyclerView.LayoutManager childLayoutManager;
    private FloatingActionButton addClanMemberFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_clan, container, false);

        final String CLAN_NAME = getResources().getString(R.string.clan_name);
        String clanName = getArguments().getString(CLAN_NAME) + CLAN;

        clanNameTextView = view.findViewById(R.id.clan_name_text_view);
        clanNameTextView.setText(clanName);

        parentRecyclerView = view.findViewById(R.id.parent_recycler_view);
        parentRecyclerView.setHasFixedSize(true);

        parentLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        parentRecyclerView.setLayoutManager(parentLayoutManager);

        parentAdapter = new ParentRecyclerViewAdapter(ParentRecyclerViewItem.getData());
        parentRecyclerView.setAdapter(parentAdapter);

        childRecyclerView = view.findViewById(R.id.child_recycler_view);
        childRecyclerView.setHasFixedSize(true);

        childLayoutManager = new LinearLayoutManager(getActivity());
        childRecyclerView.setLayoutManager(childLayoutManager);

        childAdapter = new ChildRecyclerViewAdapter(ChildRecyclerViewItem.getData());
        childRecyclerView.setAdapter(childAdapter);

        addClanMemberFab = view.findViewById(R.id.add_clan_member_fab);
        addClanMemberFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add a new member, disable button
            }
        });

        return view;
    }
}
