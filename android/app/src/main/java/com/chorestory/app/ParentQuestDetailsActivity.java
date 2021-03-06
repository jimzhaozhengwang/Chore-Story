package com.chorestory.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.helpers.QuestCompletion;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.model.QuestRecyclerViewItem;
import com.chorestory.templates.GetQuestResponse;
import com.chorestory.templates.QuestCreateResponse;
import com.chorestory.templates.QuestModifyRequest;
import com.chorestory.templates.SingleResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentQuestDetailsActivity extends ChoreStoryActivity {

    @Inject
    RetrofitInterface retrofitInference;
    @Inject
    TokenHandler tokenHandler;

    private EditText questNameEditText;
    private ImageView questImageView;
    private EditText expEditText;
    private TextView statusTextView;
    private ImageView statusImageView;
    private Button viewProofButton;
    private Button selectDateButton;
    private DatePickerDialog datePickerDialog;
    private Button selectTimeButton;
    private TextView questOwnerTextView;
    private TextView recurrenceValueTextView;
    private EditText descriptionEditText;
    private Button cancelButton;
    private Button saveButton;
    private Button deleteButton;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String standardDate;
    private String standardTime;
    private String questName;
    private String recurrence;
    private Activity activity = this;
    private QuestRecyclerViewItem quest;
    private String questOwnerName;
    private int questOwnerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_quest_details);
        getSupportActionBar().setTitle(getString(R.string.quest_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // prevent keyboard from opening on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        questNameEditText = findViewById(R.id.quest_name_edit_text);
        questImageView = findViewById(R.id.quest_image_view);
        expEditText = findViewById(R.id.quest_exp_edit_text);
        statusTextView = findViewById(R.id.quest_status_text_view);
        statusImageView = findViewById(R.id.quest_status_image_view);
        viewProofButton = findViewById(R.id.view_proof_button);
        selectDateButton = findViewById(R.id.quest_select_date_button);
        selectTimeButton = findViewById(R.id.quest_select_time_button);
        questOwnerTextView = findViewById(R.id.quest_owner_text_view_value);
        recurrenceValueTextView = findViewById(R.id.quest_recurrence_value_text_view);
        descriptionEditText = findViewById(R.id.description_edit_text);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);

        // Select Date
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDay = dayOfMonth;
                        mMonth = month;
                        mYear = year;
                        standardDate = String.format("%02d-%02d-%02d", mYear, mMonth + 1, mDay);
                        selectDateButton.setText(getDateText());
                    }
                };
                datePickerDialog = new DatePickerDialog(activity, listener, year, month, day);
                datePickerDialog.updateDate(mYear, mMonth, mDay);
                datePickerDialog.show();
                enableButtons();
            }
        });

        // Select Time
        selectTimeButton.setText(getTimeText());
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // TODO: consider using AM/PM instead
                                mHour = hourOfDay;
                                mMinute = minute;
                                standardTime = String.format("%02d:%02d", mHour, mMinute);
                                selectTimeButton.setText(getTimeText());
                            }
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(activity));
                timePickerDialog.updateTime(mHour, mMinute);
                timePickerDialog.show();
                enableButtons();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtons();
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtons();
                onBackPressed();
                onSaveClick();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtons();
                onBackPressed();
                onDeleteClick();
            }
        });
    }

    private void populateFields() {

        // Quest Name
        questNameEditText.setText(quest.getName());

        // Quest icon
        questImageView.setImageResource(quest.getImageId());

        // Quest Exp
        expEditText.setText(Integer.toString(quest.getExp()));

        // status
        QuestCompletion status = quest.getStatus();
        String statusString = quest.getQuestCompletionString(status);

        // hiding this because we're not doing proof for now
        viewProofButton.setVisibility(viewProofButton.GONE);

        statusTextView.setText(statusString);
        int statusImage;
        if (status == QuestCompletion.PENDING) {
            statusImage = R.drawable.yellow_check_mark;
        } else if (status == QuestCompletion.COMPLETED) {
            viewProofButton.setVisibility(viewProofButton.GONE);
            statusImage = R.drawable.green_check_mark;
        } else {
            viewProofButton.setVisibility(viewProofButton.GONE);
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
        standardTime = String.format("%02d:%02d", mHour, mMinute);
        standardDate = String.format("%02d-%02d-%02d", mYear, mMonth + 1, mDay);

        selectDateButton.setText(getDateText());
        selectTimeButton.setText(getTimeText());

        // TODO: make this a dropdown so you can edit it
        // Recurrence
        recurrenceValueTextView.setText(quest.getRecurrenceText());

        // Child
        // TODO: make this a dropdown (would have to make a clan request)
        questOwnerTextView.setText(questOwnerName);

        // Description
        descriptionEditText.setText(quest.getDescription());

    }

    public void onSaveClick() {
        // name
        String newQuestName = questNameEditText.getText().toString();

        //exp
        int newExp = Integer.parseInt(expEditText.getText().toString());

        // DueDate
        String standardDateTime = standardDate + " " + standardTime;
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dueDate = new Date();
        try {
            dueDate = format.parse(standardDateTime);
        } catch (ParseException e) {
            Log.d("BUG", "Parse exception: ");
            Log.d("BUG", e.getMessage());
        }
        long newDueDate = dueDate.getTime() / 1000;

        // Description
        String newDescription = descriptionEditText.getText().toString();

        // Create the retrofit request containing all quest information
        QuestModifyRequest questModifyRequest = new QuestModifyRequest(
                questOwnerId,
                newQuestName,
                newDescription,
                newExp,
                quest.getNeedsVerification(),
                quest.getTimestamp(),
                newDueDate
        );
        String token = tokenHandler.getToken(this);
        Call<QuestCreateResponse> questModifyQuery = retrofitInference.modify_quest(
                token,
                quest.getId(),
                questModifyRequest
        );

        questModifyQuery.enqueue(new Callback<QuestCreateResponse>() {
            @Override
            public void onResponse(Call<QuestCreateResponse> call, Response<QuestCreateResponse> response) {
                if (response.isSuccessful()) {
                    Toaster.showToast(getApplicationContext(), "Successfully modified quest!");
                } else {
                    Toaster.showToast(getApplicationContext(), "Something went wrong.");
                }
            }

            @Override
            public void onFailure(Call<QuestCreateResponse> call, Throwable t) {
                Toaster.showToast(getApplicationContext(), "Something went wrong.");
            }
        });
    }

    private void onDeleteClick() {
        String token = tokenHandler.getToken(this);

        Call<SingleResponse<Boolean>> questDeleteQuery = retrofitInference.delete_quest(
                token,
                quest.getId()
        );

        questDeleteQuery.enqueue(new Callback<SingleResponse<Boolean>>() {
            @Override
            public void onResponse(Call<SingleResponse<Boolean>> call, Response<SingleResponse<Boolean>> response) {
                if (response.isSuccessful()) {
                    Toaster.showToast(getApplicationContext(), "Successfully deleted quest!");
                } else {
                    Toaster.showToast(getApplicationContext(), "Something went wrong.");
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Boolean>> call, Throwable t) {
                Toaster.showToast(getApplicationContext(), "Something went wrong.");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String token = tokenHandler.getToken(this);
        if (token != null) {
            if (tokenHandler.isParentToken(token)) {
                int qid = getIntent().getIntExtra(getResources().getString(R.string.qid), 1);
                questOwnerName = getIntent().getStringExtra(getResources().getString(R.string.owner_name));
                questOwnerId = getIntent().getIntExtra(getResources().getString(R.string.owner_id), 1);
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
        // mMonth is represented by an integer from 0 to 11
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
