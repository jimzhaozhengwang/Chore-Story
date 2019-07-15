package com.chorestory.fragment;

import android.support.v4.app.Fragment;
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
}
