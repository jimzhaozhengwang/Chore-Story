package com.chorestory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.adapter.ChildRecyclerViewAdapter;
import com.chorestory.adapter.ParentRecyclerViewAdapter;
import com.chorestory.app.App;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.ClanChildrenResponse;
import com.chorestory.templates.ClanResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentClanFragment extends ChoreStoryFragment {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    private TextView clanNameTextView;
    private RecyclerView parentRecyclerView;
    private RecyclerView.Adapter parentAdapter;
    private RecyclerView.LayoutManager parentLayoutManager;
    private RecyclerView childRecyclerView;
    private RecyclerView.Adapter childAdapter;
    private RecyclerView.LayoutManager childLayoutManager;
    private FloatingActionButton addClanMemberFab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_clan, container, false);
        App.getAppComponent().inject(this);

        clanNameTextView = view.findViewById(R.id.clan_name_text_view);

        parentRecyclerView = view.findViewById(R.id.parent_recycler_view);
        parentRecyclerView.setHasFixedSize(true);

        parentLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        parentRecyclerView.setLayoutManager(parentLayoutManager);

        childRecyclerView = view.findViewById(R.id.child_recycler_view);
        childRecyclerView.setHasFixedSize(true);

        childLayoutManager = new LinearLayoutManager(getActivity());
        childRecyclerView.setLayoutManager(childLayoutManager);

        addClanMemberFab = view.findViewById(R.id.add_clan_member_fab);

        views = Collections.singletonList(addClanMemberFab);

        enableViews();

        addClanMemberFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add a new member
                disableViews();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Populate clan info
        String token = tokenHandler.getToken(getContext());
        if (token != null) {
            if (tokenHandler.isParentToken(token)) {
                // Get parent and generic clan information
                Call<ClanResponse> clanQuery = retrofitInterface.get_clan(token);
                clanQuery.enqueue(new Callback<ClanResponse>() {
                    @Override
                    public void onResponse(Call<ClanResponse> call, Response<ClanResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {

                            ClanResponse.Data respData = response.body().getData();

                            // Set the clan Title
                            clanNameTextView.setText(getString(R.string.clan_title, respData.getClanName()));

                            // Set the parent list
                            List<ClanResponse.Data.Parent> parentList = respData.getParents();
                            parentAdapter = new ParentRecyclerViewAdapter(parentList);
                            parentRecyclerView.setAdapter(parentAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ClanResponse> call, Throwable t) {
                        Toaster.showToast(getContext(), "Internal error occurred.");
                        deleteTokenNavigateMain(getContext());
                    }
                });

                // Get Children in clan information
                Call<ClanChildrenResponse> clanChildrenQuery = retrofitInterface.get_clan_children(token);
                clanChildrenQuery.enqueue(new Callback<ClanChildrenResponse>() {
                    @Override
                    public void onResponse(Call<ClanChildrenResponse> call, Response<ClanChildrenResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {

                            // Set the child list
                            List<ClanChildrenResponse.Children> childrenList = response.body().getChildren();
                            childAdapter = new ChildRecyclerViewAdapter(childrenList);
                            childRecyclerView.setAdapter(childAdapter);

                        }
                    }

                    @Override
                    public void onFailure(Call<ClanChildrenResponse> call, Throwable t) {
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
