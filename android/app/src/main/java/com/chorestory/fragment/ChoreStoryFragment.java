package com.chorestory.fragment;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.view.View;

import java.util.List;

abstract public class ChoreStoryFragment extends Fragment {
    List<View> views;

    @Override
    public void onResume() {
        super.onResume();
        enableViews();
    }

    protected void setViewsEnabled(Boolean setTo) {
        if (views != null) {
            for (View view : views) {
                view.setEnabled(setTo);
            }
        }
    }

    protected void enableViews() {
        setViewsEnabled(true);
    }

    protected void disableViews() {
        setViewsEnabled(false);
    }

    protected void navigateTo(Context context, Class<?> toClass) {
        startActivity(new Intent(context, toClass));
    }

    protected void navigateTo(Context context, Class<?> toClass, String key, String value) {
        Intent intent = new Intent(context, toClass);
        intent.putExtra(key, value);
        startActivity(intent);
    }
}
