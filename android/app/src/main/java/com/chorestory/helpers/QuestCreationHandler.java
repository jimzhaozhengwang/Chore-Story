package com.chorestory.helpers;

import android.app.Activity;

import com.chorestory.R;

public class QuestCreationHandler {
    public static boolean canCreateQuest(Activity activity,
                                         int child,
                                         String questType,
                                         int exp,
                                         int year,
                                         int month,
                                         int day,
                                         int hour,
                                         int minute,
                                         String recurrenceType) {

        // TODO: think of messages; make them string resources
        if (child == -1) {
            Toaster.showToast(activity, "Please select a child");
            return false;
        } else if (questType == null || questType.isEmpty()) {
            Toaster.showToast(activity, "Please enter a quest");
            return false;
        } else if (exp < 0) {
            Toaster.showToast(activity, "Please enter an exp");
            return false;
        } else if (year < 0 || month < 0 || day < 0) {
            Toaster.showToast(activity, "Please select a date");
            return false;
        } else if (hour < 0 || minute < 0) {
            Toaster.showToast(activity, "Please select a time");
            return false;
        } else if (recurrenceType == null) {
            Toaster.showToast(activity, "Please select a recurrence type");
            return false;
        }

        return true;
    }
}
