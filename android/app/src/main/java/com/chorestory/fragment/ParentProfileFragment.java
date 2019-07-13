package com.chorestory.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;

public class ParentProfileFragment extends Fragment {

    ImageView parentImageView;
    TextView parentNameTextView;
    TextView clanNameTextView;
    Button addMemberButton;
    Button leaveClanButton;
    TextView parentEmailTextView;
    Button changePasswordButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_profile, container, false);

        parentImageView = view.findViewById(R.id.parent_image_view);
        parentNameTextView = view.findViewById(R.id.parent_name_text_view);
        clanNameTextView = view.findViewById(R.id.clan_name_text_view);
        addMemberButton = view.findViewById(R.id.add_member_button);
        leaveClanButton = view.findViewById(R.id.leave_clan_button);
        parentEmailTextView = view.findViewById(R.id.parent_email_text_view);
        changePasswordButton = view.findViewById(R.id.change_password_button);

        // TODO: fetch parent info; replace mocked
        parentImageView.setImageResource(R.drawable.king_color);
        parentNameTextView.setText("David");
        clanNameTextView.setText("CS449");

        enableButtons();

        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: add a member, camera QR code?
            }
        });

        leaveClanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: determine appropriate behaviour
            }
        });

        parentEmailTextView.setText("hello@world.com");

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                // TODO: change user password
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableButtons();
    }

    private void enableButtons() {
        addMemberButton.setEnabled(true);
        leaveClanButton.setEnabled(true);
        changePasswordButton.setEnabled(true);
    }

    private void disableButtons() {
        addMemberButton.setEnabled(false);
        leaveClanButton.setEnabled(false);
        changePasswordButton.setEnabled(false);
    }
}
