package com.chorestory.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.adapter.ChildRecyclerViewAdapter;
import com.chorestory.app.App;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AddFriendResponse;
import com.chorestory.templates.ClanChildrenResponse;
import com.chorestory.templates.ClanChildrenResponse.Children;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildFriendsFragment extends ChoreStoryFragment {
    @Inject
    RetrofitInterface retrofitInference;
    @Inject
    TokenHandler tokenHandler;

    private List<Children> childrenList;
    private RecyclerView childRecyclerView;
    private RecyclerView.LayoutManager childLayoutManager;
    private RecyclerView.Adapter childAdapter;
    private Button addFriendButton;
    private EditText addFriendEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_friends, container, false);

        App.getAppComponent().inject(this);

        childRecyclerView = view.findViewById(R.id.child_recycler_view);
        addFriendButton = view.findViewById(R.id.add_friend_button);
        addFriendEditText = view.findViewById(R.id.add_friend_edit_text);
        childRecyclerView.setHasFixedSize(true);

        childLayoutManager = new LinearLayoutManager(getActivity());
        childRecyclerView.setLayoutManager(childLayoutManager);


        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = addFriendEditText.getText().toString();
                addFriend(username);
            }
        });

        return view;
    }

    private void addFriend(String username) {
        String token = tokenHandler.getToken(getContext());
        if (token != null) {
            if (tokenHandler.isChildToken(token)) {
                Call<AddFriendResponse> addFriendQuery = retrofitInference.add_friend(token, username);

                addFriendQuery.enqueue(new Callback<AddFriendResponse>() {
                    @Override
                    public void onResponse(Call<AddFriendResponse> call, Response<AddFriendResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                            Toaster.showToast(getContext(), "Successfully added friend!");
                            addFriendEditText.setText("");
                            getChildren();
                        } else {
                            // TODO: friend doesn't exist
                            Toaster.showToast(getContext(), "Friend not found");
                        }
                    }

                    @Override
                    public void onFailure(Call<AddFriendResponse> call, Throwable t) {
                        // TODO: delete the token we have stored and redirect the user to the login page?
                    }
                });
            } else {
                // TODO: redirect to login page
            }
        }
    }

    private void getChildren() {
        String token = tokenHandler.getToken(getContext());
        if (token != null) {
            if (tokenHandler.isChildToken(token)) {
                Call<ClanChildrenResponse> getFriendsQuery = retrofitInference.list_friends(token);

                getFriendsQuery.enqueue(new Callback<ClanChildrenResponse>() {
                    @Override
                    public void onResponse(Call<ClanChildrenResponse> call, Response<ClanChildrenResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                            childrenList = response.body().getChildren();
                            childAdapter = new ChildRecyclerViewAdapter(childrenList);
                            childRecyclerView.setAdapter(childAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ClanChildrenResponse> call, Throwable t) {
                        // TODO: delete the token we have stored and redirect the user to the login page?
                    }
                });
            } else {
                // TODO: redirect to login page
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getChildren();
    }
}

