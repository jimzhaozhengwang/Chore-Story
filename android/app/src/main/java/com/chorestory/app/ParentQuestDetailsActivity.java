package com.chorestory.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.chorestory.R;

import java.util.Calendar;

public class ParentQuestDetailsActivity extends ChoreStoryActivity {

    private Spinner questNameSpinner;
    private EditText expEditText;
    private Button selectDateButton;
    private Button selectTimeButton;
    private Spinner selectChildSpinner;
    private Spinner selectRecurrenceSpinner;
    private EditText descriptionEditText;
    private String questType;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String recurrence;
    private Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setContentView(R.layout.activity_parent_quest_details);
        getSupportActionBar().setTitle(getString(R.string.quest_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // prevent keyboard from opening on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        questNameSpinner = findViewById(R.id.quest_name_spinner);
        expEditText = findViewById(R.id.quest_exp_edit_text);
        selectDateButton = findViewById(R.id.quest_select_date_button);
        selectTimeButton = findViewById(R.id.quest_select_time_button);
        selectChildSpinner = findViewById(R.id.select_child_spinner);
        selectRecurrenceSpinner = findViewById(R.id.select_recurrence_spinner);
        descriptionEditText = findViewById(R.id.description_edit_text);

        // TODO set all initial values to actual quest info
        mYear = 1996;
        mMonth = 10;
        mDay = 29;
        mHour = 5;
        mMinute = 30;

        // Quest Name
        ArrayAdapter<CharSequence> questSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.quest_array, R.layout.spinner_item);
        questSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questNameSpinner.setAdapter(questSpinnerAdapter);
        // TODO: fetch actual value for quest name
        // TODO: this initial setSelection isn't working idk why
        selectRecurrenceSpinner.setSelection(questSpinnerAdapter.getPosition("Doing laundry"));
        questNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questType = (String) parent.getItemAtPosition(position);

                if (questType != null && !questType.equals(getString(R.string.select_a_quest))) {
                    int exp = 30; // TODO: figure out an appropriate exp for each quest
                    expEditText.setText(Integer.toString(exp));
                } else {
                    expEditText.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                questType = null;
            }
        });

        // Quest Exp
        // TODO: fetch actual value
        // TODO: this initial setText isn't working idk why
        expEditText.setText("10");

        // TODO: selectChildSpinner

        // Select Date
        selectDateButton.setText(getDateText());
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons(); // ?
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
                        selectDateButton.setText(getDateText());
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, listener, year, month, day);
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
                                selectTimeButton.setText(getTimeText());
                            }
                        },
                        hour,
                        minute,
                        DateFormat.is24HourFormat(activity));
                timePickerDialog.show();
                enableButtons();
            }
        });

        // Recurrence Spinner
        ArrayAdapter<CharSequence> recurrenceSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.recurrence_array, R.layout.spinner_item);
        recurrenceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectRecurrenceSpinner.setAdapter(recurrenceSpinnerAdapter);
        // Todo: fetch actual value for the recurrence string
        selectRecurrenceSpinner.setSelection(recurrenceSpinnerAdapter.getPosition("Repeats monthly"));
        selectRecurrenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recurrence = (String) parent.getItemAtPosition(position);
                System.out.println("onItemSelected");
                System.out.println(recurrence);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recurrence = null; // TODO?
                System.out.println("onNothingSelected");
                System.out.println(recurrence);
            }
        });

        // TODO: fetch actual value
        descriptionEditText.setText("This is the description of the quest before change");

        // TODO: add Cancel and Save buttons. on Save send request to modify quest and exit activity
    }

    private String getDateText() {
        // months used are 0-11
        return mDay + "-" + (mMonth+1) + "-" + mYear;
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
