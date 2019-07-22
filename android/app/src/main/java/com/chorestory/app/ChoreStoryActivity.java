package com.chorestory.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.chorestory.helpers.TokenHandler;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

abstract public class ChoreStoryActivity extends AppCompatActivity {

    List<Button> buttons;

    InputMethodManager inputMethodManager;

    @Inject
    TokenHandler tokenHandler;

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

    protected void navigateTo(Context context, Class<?> toClass) {
        startActivity(new Intent(context, toClass));
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

    protected void deleteTokenNavigateMain(Context context) {
        tokenHandler.deleteStoredToken(context);
        navigateTo(context, MainActivity.class);
    }
}
