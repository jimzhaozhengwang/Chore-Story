package com.chorestory.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;

public class ChildProfileFragment extends ChoreStoryFragment {

    ImageView childImageView;
    TextView childUsernameTextView;
    TextView childNameTextView;
    TextView childLevelTextView;
    TextView childExpTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_profile, container, false);

        childImageView = view.findViewById(R.id.child_image_view);
        childUsernameTextView = view.findViewById(R.id.child_username_text_view);
        childNameTextView = view.findViewById(R.id.child_name_text_view);
        childLevelTextView = view.findViewById(R.id.child_level_text_view);
        childExpTextView = view.findViewById(R.id.child_exp_text_view);

        // TODO: get data
        childImageView.setImageResource(R.drawable.king_color);
        childUsernameTextView.setText("CS449jim");
        childNameTextView.setText("Jim");
        childLevelTextView.setText("10");
        childExpTextView.setText("6");

        return view;
    }
}
