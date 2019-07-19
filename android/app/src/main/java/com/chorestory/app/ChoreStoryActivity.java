package com.chorestory.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.List;
import java.util.Objects;

abstract public class ChoreStoryActivity extends AppCompatActivity {

    List<Button> buttons;

    InputMethodManager inputMethodManager;

    @Override
    protected void onResume() {
        super.onResume();
        enableButtons();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    protected void setButtons(Boolean setTo) {
        if (buttons != null) {
            for (Button button : buttons) {
                button.setEnabled(setTo);
            }
        }
    }

    protected void enableButtons() {
        setButtons(true);
    }

    protected void disableButtons() {
        setButtons(false);
    }

    protected void navigateTo(Class<?> cls, String key, String value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    public void navigateTo(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    protected void hideKeyBoard() {
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }
}
