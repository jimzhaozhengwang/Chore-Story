package com.chorestory.app;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.helpers.QuestCompletion;

public class ChildQuestDetailsActivity extends ChoreStoryActivity {

    private TextView questNameTextView;
    private ImageView questImageView;
    private TextView expTextView;
    private TextView statusTextView;
    private ImageView statusImageView;
    private Button addProofButton;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView recurrenceTextView;
    private TextView questDescriptionTextView;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String questName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_child_quest_details);
        getSupportActionBar().setTitle(getString(R.string.quest_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // prevent keyboard from opening on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        questNameTextView = findViewById(R.id.quest_name_text_view);
        questImageView = findViewById(R.id.quest_image_view);
        expTextView = findViewById(R.id.quest_exp_text_view);
        statusTextView = findViewById(R.id.quest_status_text_view);
        statusImageView = findViewById(R.id.quest_status_image_view);
        addProofButton = findViewById(R.id.add_proof_button);
        dateTextView = findViewById(R.id.quest_date_text_view);
        timeTextView = findViewById(R.id.quest_time_text_view);
        recurrenceTextView = findViewById(R.id.recurrence_text_view);
        questDescriptionTextView = findViewById(R.id.quest_description_text_view);

        // TODO set all initial values to actual quest info
        mYear = 1996;
        mMonth = 11;
        mDay = 29;
        mHour = 5;
        mMinute = 30;

        // Quest Name
        // TODO: fetch actual value
        questName = "Sweep kitchen floor";
        questNameTextView.setText(questName);

        // Quest icon
        // TODO: fetch quest icon based on name
        int icon = R.drawable.cleaner;
        questImageView.setImageResource(icon);

        // Quest Exp
        // TODO: fetch actual value
        expTextView.setText("20");

        // status
        // TODO: fetch actual value and possibly hide button
        int statusText = R.string.pending_approval;
        QuestCompletion questStatus = QuestCompletion.PENDING;
        statusTextView.setText(getString(statusText));
        int statusImage;
        if (questStatus == QuestCompletion.PENDING) {
            statusImage = R.drawable.yellow_check_mark;
            addProofButton.setVisibility(addProofButton.GONE);
        } else if (questStatus == QuestCompletion.COMPLETED) {
            statusImage = R.drawable.green_check_mark;
            addProofButton.setVisibility(addProofButton.GONE);
        } else {
            statusImage = R.drawable.red_check_mark;
        }
        statusImageView.setImageResource(statusImage);

        // Date
        dateTextView.setText(getDateText());

        // Time
        timeTextView.setText(getTimeText());

        // Recurrence
        recurrenceTextView.setText("Repeat once a week");

        // TODO: fetch actual value
        questDescriptionTextView.setText("This is the description of the quest");
    }

    private String getDateText() {
        return mDay + "-" + mMonth + "-" + mYear;
    }

    private String getTimeText() {
        return String.format("%02d:%02d", mHour, mMinute);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
