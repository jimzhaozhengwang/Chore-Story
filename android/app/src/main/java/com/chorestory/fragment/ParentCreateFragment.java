package com.chorestory.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.viewpager.widget.ViewPager;

import com.chorestory.Interface.RetrofitInterface;
import com.chorestory.R;
import com.chorestory.adapter.HomeAdapter;
import com.chorestory.app.App;
import com.chorestory.helpers.QuestCreationHandler;
import com.chorestory.helpers.Toaster;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.templates.AccountResponse;
import com.chorestory.templates.AccountResponse.Data.Child;
import com.chorestory.templates.QuestCreateRequest;
import com.chorestory.templates.QuestCreateResponse;
import com.chorestory.templates.QuestDialogFlowRequest;
import com.chorestory.templates.QuestDialogFlowResponse;
import com.chorestory.templates.TimeTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ParentCreateFragment extends ChoreStoryFragment {

    private final int SPEECH_REQUEST_CODE = 100;

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;

    private Spinner childSpinner;
    private ImageButton micImageButton;
    private EditText questEditText;
    private TextView expTextView;
    private TextView dateTextView;
    private Button selectDateButton;
    private TextView timeTextView;
    private Button selectTimeButton;
    private Spinner recurrenceSpinner;
    private CheckBox needsVerificationCheckBox;
    private EditText descriptionEditText;
    private FloatingActionButton createQuestFab;

    private int selectedChildId;
    private String questType;
    private int exp;
    private String yearDateStandard;
    private String timeDateStandard;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String recurrenceType;
    private boolean needsVerification;
    private String description;
    private ViewPager viewPager;

    private String token;
    private List<Child> childList;

    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_create, container, false);

        App.getAppComponent().inject(this);

        token = tokenHandler.getToken(getContext());

        selectedChildId = -1;
        questType = null;
        exp = -1;
        mYear = -1;
        mMonth = -1;
        mDay = -1;
        mHour = -1;
        mMinute = -1;
        recurrenceType = null;
        needsVerification = false;
        description = "";

        childSpinner = view.findViewById(R.id.child_spinner);
        viewPager = getActivity().findViewById(R.id.view_pager);

        micImageButton = view.findViewById(R.id.mic_button);

        questEditText = view.findViewById(R.id.quest_edit_text);

        expTextView = view.findViewById(R.id.exp_text_view);
        expTextView.setText("");

        dateTextView = view.findViewById(R.id.date_text_view);
        dateTextView.setText("");
        selectDateButton = view.findViewById(R.id.select_date_button);

        timeTextView = view.findViewById(R.id.time_text_view);
        timeTextView.setText("");
        selectTimeButton = view.findViewById(R.id.select_time_button);

        recurrenceSpinner = view.findViewById(R.id.recurrence_spinner);

        needsVerificationCheckBox = view.findViewById(R.id.needs_verification_check_box);

        descriptionEditText = view.findViewById(R.id.description_edit_text);

        createQuestFab = view.findViewById(R.id.create_quest_fab);

        views = Arrays.asList(selectDateButton, selectTimeButton, createQuestFab);

        populateChildrenList();

        micImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                startActivityForResult(intent, SPEECH_REQUEST_CODE);
            }
        });

        questEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                questType = s.toString();
                if (questType.isEmpty()) {
                    exp = -1;
                    expTextView.setText("");
                } else {
                    exp = 2;
                    String expString = exp + " Exp";
                    expTextView.setText(expString);
                }
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // TODO: consider using July 1, 2019 instead
                                month++; // month is represented by an integer from 0 to 11
                                String date = dayOfMonth + "-" + month + "-" + year;
                                yearDateStandard = String.format("%02d-%02d-%02d", year, month, dayOfMonth);

                                mDay = dayOfMonth;
                                mMonth = month;
                                mYear = year;
                                dateTextView.setText(date);
                            }
                        },
                        year,
                        month,
                        day);
                datePickerDialog.show();
                enableViews();
            }
        });

        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // TODO: consider using AM/PM instead
                                String time = String.format("%02d:%02d", hourOfDay, minute);
                                timeDateStandard = String.format("%02d:%02d:%02d", hourOfDay, minute, 0);

                                mHour = hourOfDay;
                                mMinute = minute;
                                timeTextView.setText(time);
                            }
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
                enableViews();
            }
        });

        ArrayAdapter<CharSequence> recurrenceSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.recurrence_array, R.layout.spinner_item);

        recurrenceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurrenceSpinner.setAdapter(recurrenceSpinnerAdapter);

        recurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recurrenceType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recurrenceType = null;
            }
        });

        needsVerificationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needsVerification = needsVerificationCheckBox.isChecked();
            }
        });

        createQuestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                if (!QuestCreationHandler.canCreateQuest(getActivity(),
                        selectedChildId,
                        questType,
                        exp,
                        mYear,
                        mMonth,
                        mDay,
                        mHour,
                        mMinute,
                        recurrenceType)) {
                    enableViews();
                } else {

                    if (token != null && tokenHandler.isParentToken(token)) {

                        // Get the description
                        description = descriptionEditText.getText().toString();

                        // Calculate the selected duedate in UTC
                        Date dueDate = new Date();
                        try {
                            dueDate = sdf.parse(yearDateStandard + "T" + timeDateStandard);
                        } catch (ParseException e) {
                            Log.d("BUG", "Parse exception: ");
                            Log.d("BUG", e.getMessage());
                        }

                        // Calculate the timestamp if it's recurring (-1 otherwise)
                        long timestamp = getTimestamp();

                        // Create the retrofit request containing all quest information
                        QuestCreateRequest questRequest = new QuestCreateRequest(
                                questType,
                                description,
                                exp,
                                timestamp,
                                dueDate.getTime() / 1000,
                                needsVerification
                        );

                        Call<QuestCreateResponse> questCreateQuery = retrofitInterface.create_quest(
                                token,
                                selectedChildId,
                                questRequest
                        );
                        questCreateQuery.enqueue(new Callback<QuestCreateResponse>() {
                            @Override
                            public void onResponse(Call<QuestCreateResponse> call, Response<QuestCreateResponse> response) {
                                if (response.isSuccessful()) {
                                    Toaster.showToast(getContext(), "Successfully created quest!");
                                    // go to quests fragment and refresh
                                    viewPager.setCurrentItem(2);
                                    HomeAdapter homeAdapter = (HomeAdapter) viewPager.getAdapter();
                                    homeAdapter.getItem(2).onResume();
                                } else {
                                    Toaster.showToast(getContext(), "Something went wrong.");
                                }

                                // Enable the views otherwise we wont be able to click when returning from
                                //      the quest view fragment if android caches this page
                                enableViews();
                            }

                            @Override
                            public void onFailure(Call<QuestCreateResponse> call, Throwable t) {
                                Toaster.showToast(getContext(), "Something went wrong.");
                                enableViews();
                            }
                        });

                    }

                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableViews();
    }

    private void populateChildrenList() {
        if (token != null && tokenHandler.isParentToken(token)) {
            Call<AccountResponse> accountQuery = retrofitInterface.me(token);
            accountQuery.enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    if (response.isSuccessful() &&
                            response.body() != null &&
                            response.body().hasResponse()) {

                        AccountResponse.Data respData = response.body().getData();
                        childList = respData.getChildren();

                        List<String> childNameList = new ArrayList<>();
                        for (Child child : childList) {
                            childNameList.add(child.getName());
                        }

                        ArrayAdapter<String> childSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, childNameList);
                        childSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        childSpinner.setAdapter(childSpinnerAdapter);

                        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position < childList.size()) {
                                    selectedChildId = childList.get(position).getId();
                                } else {
                                    Log.d("BUG", "Our child list is inconsistent somehow.");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                selectedChildId = -1;
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    Toaster.showToast(getContext(), "Internal error occurred.");
                    deleteTokenNavigateMain(getContext());
                }
            });
        } else {
            deleteTokenNavigateMain(getContext());
        }
    }

    private int getTimestamp() {
        int timestamp;

        switch (recurrenceType) {
            case "Repeats daily":
                timestamp = 60 * 60 * 24;
                break;
            case "Repeats weekly":
                timestamp = (60 * 60 * 24) * 7;
                break;
            case "Repeats monthly":
                timestamp = ((60 * 60 * 24) * 7) * 4;
                break;
            case "Repeats yearly":
                timestamp = (((60 * 60 * 24) * 7) * 4) * 12;
                break;
            default:
                timestamp = -1;
        }
        return timestamp;
    }

    private void callDialogFlow(String dialogText) {
        if (token != null && tokenHandler.isParentToken(token)) {
            QuestDialogFlowRequest questDialogFlowRequest = new QuestDialogFlowRequest(dialogText);

            Call<QuestDialogFlowResponse> questDialogQuery = retrofitInterface.get_quest_dialog_flow(token, questDialogFlowRequest);
            questDialogQuery.enqueue(new Callback<QuestDialogFlowResponse>() {
                @Override
                public void onResponse(Call<QuestDialogFlowResponse> call, Response<QuestDialogFlowResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {

                        QuestDialogFlowResponse.Data respData = response.body().getData();

                        // Populate the fields we can find in the response

                        // Check for child name
                        if (respData.hasChild()) {
                            int position = 0;
                            for (Child child : fragmentChildList) {
                                String currentName = child.getName().toLowerCase();
                                if (currentName.equals(respData.getChild().toLowerCase())) {
                                    childSpinner.setSelection(position);
                                }
                                position++;
                            }
                        }

                        // Check for quest description
                        if (respData.hasQuest()) {
                            questEditText.setText(respData.getQuest());
                        }

                        // Check for exp value
                        if (respData.hasExp()) {
                            expTextView.setText(respData.getExp().toString());
                        }

                        // Set end date
                        if (respData.hasTime()) {
                            TimeTemplate endDate = respData.getTime().getEndDateTime();
                            if (endDate != null) {
                                dateTextView.setText(
                                        endDate.getDay() + "-" +
                                        endDate.getMonth() + "-" +
                                        endDate.getYear());
                                timeTextView.setText(
                                        endDate.getHour() + ":" +
                                        endDate.getMinute());

                                // Set the values for form submission
                                yearDateStandard = String.format(
                                        "%02d-%02d-%02d",
                                        endDate.getYear(),
                                        endDate.getMonth(),
                                        endDate.getDay());
                                timeDateStandard = String.format(
                                        "%02d:%02d:%02d",
                                        endDate.getHour(),
                                        endDate.getMinute(),
                                        0);

                                // Set the values for error checking
                                mDay = endDate.getDay();
                                mMonth = endDate.getMonth();
                                mYear = endDate.getYear();

                                mHour = endDate.getHour();
                                mMinute = endDate.getMinute();
                            }
                        }

                    } else {
                        Toaster.showToast(getContext(), "Internal error occurred.");
                    }
                }

                @Override
                public void onFailure(Call<QuestDialogFlowResponse> call, Throwable t) {
                    Toaster.showToast(getContext(), "Internal error occurred.");
                }
            });
        } else {
            deleteTokenNavigateMain(getContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callDialogFlow("Add a quest to draw an octahedron on Tuesday evening for John for 5 points");

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            callDialogFlow(spokenText);

            Toaster.showToast(getContext(), spokenText);
        } else {
            Toaster.showToast(getContext(), getString(R.string.unable_to_recognize_speech));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
