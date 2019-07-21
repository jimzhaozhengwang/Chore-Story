package com.chorestory.app;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.QuestCompletion;
import com.chorestory.model.QuestRecyclerViewItem;
import com.chorestory.templates.GetQuestResponse;
import com.chorestory.helpers.TokenHandler;

import java.util.Calendar;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildQuestDetailsActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInference;
    @Inject
    TokenHandler tokenHandler;

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
    private QuestRecyclerViewItem quest;

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
    }

    private void populateFields() {

        // Quest Name
        questNameTextView.setText(quest.getName());

        // Quest icon
        questImageView.setImageResource(quest.getImageId());

        // Quest Exp
        expTextView.setText(Integer.toString(quest.getExp()));

        // status
        QuestCompletion status = quest.getStatus();
        String statusString = quest.getQuestCompletionString(status);

        // hiding this because we're not doing proof for now
        addProofButton.setVisibility(addProofButton.GONE);

        statusTextView.setText(statusString);
        int statusImage;
        if (status == QuestCompletion.PENDING) {
            statusImage = R.drawable.yellow_check_mark;
            addProofButton.setVisibility(addProofButton.GONE);
        } else if (status == QuestCompletion.COMPLETED) {
            statusImage = R.drawable.green_check_mark;
            addProofButton.setVisibility(addProofButton.GONE);
        } else {
            statusImage = R.drawable.red_check_mark;
        }
        statusImageView.setImageResource(statusImage);

        // Date and Time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) quest.getDueDate() * 1000);

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        dateTextView.setText(getDateText());
        timeTextView.setText(getTimeText());

        // Recurrence
        recurrenceTextView.setText(quest.getRecurrenceText());

        // Description
        questDescriptionTextView.setText(quest.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = tokenHandler.getToken(this);
        if (token != null) {
            if (tokenHandler.isChildToken(token)) {
                int qid = getIntent().getIntExtra(getResources().getString(R.string.qid), 1);
                int ts = getIntent().getIntExtra(getResources().getString(R.string.ts), -1);
                Call<GetQuestResponse> getQuestQuery;
                if (ts == -1) {
                    getQuestQuery = retrofitInference.get_quest(token, qid);
                } else {
                    ts -= 60 * 5; // the response uses math.round so subtract 5 minutes to be safe
                    getQuestQuery = retrofitInference.get_quest_ts(token, qid, ts + ".0");
                }
                getQuestQuery.enqueue(new Callback<GetQuestResponse>() {
                    @Override
                    public void onResponse(Call<GetQuestResponse> call, Response<GetQuestResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {
                            quest = new QuestRecyclerViewItem(response.body().getQuest());
                            populateFields();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetQuestResponse> call, Throwable t) {
                        // TODO: delete the token we have stored and redirect the user to the login page?
                    }
                });
            } else {
                // TODO: redirect to login page
            }
        }
    }

    private String getDateText() {
        return mDay + "-" + (mMonth + 1) + "-" + mYear;
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
