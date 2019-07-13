package com.chorestory.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
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

import com.chorestory.R;
import com.chorestory.helpers.QuestCreationHandler;

import java.util.Arrays;
import java.util.Calendar;

public class ParentCreateFragment extends ChoreStoryFragment {

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

    private String child;
    private String questType;
    private int exp;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String recurrenceType;
    private boolean mandatory;
    private String description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_create, container, false);

        child = null;
        questType = null;
        exp = -1;
        mYear = -1;
        mMonth = -1;
        mDay = -1;
        mHour = -1;
        mMinute = -1;
        recurrenceType = null;
        description = null;

        childSpinner = view.findViewById(R.id.child_spinner);

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

        views = Arrays.asList(selectDateButton, selectTimeButton, createQuestFab);

        enableViews();

        // TODO: fetch children and replace child_array with children
        ArrayAdapter<CharSequence> childSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.child_array, R.layout.spinner_item);

        childSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childSpinner.setAdapter(childSpinnerAdapter);

        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                child = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                child = null;
            }
        });

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
                        child,
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
                    // TODO: create quest, pass the following info
                    // String child;
//                    String questType;
//                    int exp;
//                    int mYear;
//                    int mMonth;
//                    int mDay;
//                    int mHour;
//                    int mMinute;
//                    String recurrenceType;
//                    boolean mandatory;
//                    @Nullable String description;
                }
            }
        });
        return view;
    }
}
