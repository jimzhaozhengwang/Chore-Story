package com.chorestory.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentCreateFragment extends ChoreStoryFragment {

    @Inject
    RetrofitInterface retrofitInterface;
    @Inject
    TokenHandler tokenHandler;


    private Spinner childSpinner;
    private Spinner questSpinner;
    private TextView expTextView;
    private TextView dateTextView;
    private Button selectDateButton;
    private TextView timeTextView;
    private Button selectTimeButton;
    private Spinner recurrenceSpinner;
    private CheckBox mandatoryCheckBox;
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
    private boolean mandatory;
    private String description;
    private ViewPager viewPager;

    private String token;
    private List<Child> fragmentChildList;

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
        description = "";

        childSpinner = view.findViewById(R.id.child_spinner);
        viewPager = getActivity().findViewById(R.id.view_pager);

        questSpinner = view.findViewById(R.id.quest_spinner);

        expTextView = view.findViewById(R.id.exp_text_view);
        expTextView.setText("");

        dateTextView = view.findViewById(R.id.date_text_view);
        dateTextView.setText("");
        selectDateButton = view.findViewById(R.id.select_date_button);

        timeTextView = view.findViewById(R.id.time_text_view);
        timeTextView.setText("");
        selectTimeButton = view.findViewById(R.id.select_time_button);

        recurrenceSpinner = view.findViewById(R.id.recurrence_spinner);

        mandatoryCheckBox = view.findViewById(R.id.mandatory_check_box);

        descriptionEditText = view.findViewById(R.id.description_edit_text);

        createQuestFab = view.findViewById(R.id.create_quest_fab);

        populateChildrenList();

        ArrayAdapter<CharSequence> questSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.quest_array, R.layout.spinner_item);

        questSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questSpinner.setAdapter(questSpinnerAdapter);

        questSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questType = (String) parent.getItemAtPosition(position);

                if (questType != null && !questType.equals(getString(R.string.select_a_quest))) {
                    exp = 30; // TODO: figure out an appropriate exp for each quest
                    String expString = exp + " Exp";
                    expTextView.setText(expString);
                } else {
                    exp = -1;
                    expTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                questType = null;
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

        mandatoryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mandatory = mandatoryCheckBox.isChecked();
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
                                mandatory
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
                                    // TODO: re-enable buttons
                                }
                            }

                            @Override
                            public void onFailure(Call<QuestCreateResponse> call, Throwable t) {
                                Toaster.showToast(getContext(), "Something went wrong.");
                                // TODO: re-enable buttons
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
        enableButtons();
    }

    // TODO: create ChoreStoryFragment as super class
    private void disableButtons() {
        selectDateButton.setEnabled(false);
        selectTimeButton.setEnabled(false);
        createQuestFab.setEnabled(false);
    }

    private void enableButtons() {
        selectDateButton.setEnabled(true);
        selectTimeButton.setEnabled(true);
        createQuestFab.setEnabled(true);
    }

    private void populateChildrenList() {

        if (token != null && tokenHandler.isParentToken(token)) {
            Call<AccountResponse> accountQuery = retrofitInterface.me(token);
            accountQuery.enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().hasResponse()) {

                        AccountResponse.Data respData = response.body().getData();
                        fragmentChildList = respData.getChildren();

                        List<String> childNameList = new ArrayList<>();
                        for (Child child : fragmentChildList) {
                            childNameList.add(child.getName());
                        }

                        ArrayAdapter<String> childSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, childNameList);
                        childSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        childSpinner.setAdapter(childSpinnerAdapter);

                        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position < fragmentChildList.size()) {
                                    selectedChildId = fragmentChildList.get(position).getId();
                                } else {
                                    Log.d("BUG", "Our child list is inconsistent somehow.");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                selectedChildId = 0;
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    Toaster.showToast(getContext(), "Internal error occurred.");
                    // TODO: delete the token we have stored and redirect the user to the login page
                }
            });
        } else {
            // TODO: delete the token and redirect user to login page?
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
}
