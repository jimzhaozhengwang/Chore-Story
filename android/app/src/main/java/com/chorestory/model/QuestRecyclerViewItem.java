package com.chorestory.model;

import android.text.format.DateUtils;

import com.chorestory.R;
import com.chorestory.helpers.QuestCompletion;
import com.chorestory.templates.GetQuestsResponse;

import java.util.ArrayList;
import java.util.List;

public class QuestRecyclerViewItem implements Comparable<QuestRecyclerViewItem> {

    private String name;
    private int exp;
    private String owner;
    private int ownerId;
    private String description;
    private boolean mandatory;
    private int dueDate;
    private boolean recurring;
    private int nextOccurrence;
    private int id;
    private boolean needsVerification;
    private int completedOn;
    private int verifiedOn;
    private int timestamp;

    public static List<QuestRecyclerViewItem> getData(List<QuestParcelable> questParcelables) {
        List<QuestRecyclerViewItem> dataList = new ArrayList<>();
        for (int i = 0; i < questParcelables.size(); i++) {
            QuestRecyclerViewItem item = new QuestRecyclerViewItem(questParcelables.get(i));
            dataList.add(item);
        }
        return dataList;
    }

    QuestRecyclerViewItem(QuestParcelable quest) {
        this.name = quest.getTitle();
        this.exp = quest.getReward();
        this.owner = quest.getOwnerName();
        this.ownerId = quest.getOwnerId();
        this.description = quest.getDescription();
        this.mandatory = true;
        this.dueDate = quest.getDue();
        this.recurring = quest.getReccurring();
        this.nextOccurrence = quest.getNextOccurrence();
        this.id = quest.getId();
        this.needsVerification = quest.getNeedsVerification();
        this.completedOn = quest.getCompletedOn();
        this.verifiedOn = quest.getVerifiedOn();
    }

    public QuestRecyclerViewItem(GetQuestsResponse.Quest quest) {
        this.name = quest.getTitle();
        this.exp = quest.getReward();
        this.owner = null;
        this.description = quest.getDescription();
        this.mandatory = true;
        this.dueDate = quest.getDue();
        this.recurring = quest.getRecurring();
        this.nextOccurrence = quest.getNextOccurrence();
        this.timestamp = quest.getTimestamp();
        this.id = quest.getId();
        this.needsVerification = quest.getNeedsVerification();
        this.completedOn = quest.getCompletedOn();
        this.verifiedOn = quest.getVerifiedOn();
    }

    public String getName() {
        return name;
    }

    private boolean stringContainsItemFromList(String inputStr, String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (inputStr.contains(items[i])) {
                return true;
            }
        }
        return false;
    }

    public int getImageId() {
        String lowerName = name.toLowerCase();
        int imageId;
        if (stringContainsItemFromList(lowerName, new String[]{"laundry", "washer", "dryer"}))
            imageId = R.drawable.washing_machine;
        else if (stringContainsItemFromList(lowerName, new String[]{"fold"}))
            imageId = R.drawable.laundry;
        else if (stringContainsItemFromList(lowerName, new String[]{"dish"}))
            imageId = R.drawable.washing_plate;
        else if (stringContainsItemFromList(lowerName, new String[]{"meal", "food"}))
            imageId = R.drawable.dish;
        else if (stringContainsItemFromList(lowerName, new String[]{"dust"}))
            imageId = R.drawable.dusting;
        else if (stringContainsItemFromList(lowerName, new String[]{"mop"}))
            imageId = R.drawable.floor_mop;
        else if (stringContainsItemFromList(lowerName, new String[]{"paint"}))
            imageId = R.drawable.paint;
        else if (stringContainsItemFromList(lowerName, new String[]{"garbage", "trash", "rubbish", "recycl", "throw"}))
            imageId = R.drawable.garbage;
        else if (stringContainsItemFromList(lowerName, new String[]{"plant", "garden"}))
            imageId = R.drawable.garden_hose;
        else if (stringContainsItemFromList(lowerName, new String[]{"iron", "press"}))
            imageId = R.drawable.iron;
        else if (stringContainsItemFromList(lowerName, new String[]{"lawn", "grass", "mow"}))
            imageId = R.drawable.lawn_mower;
        else if (stringContainsItemFromList(lowerName, new String[]{"paint"}))
            imageId = R.drawable.paint;
        else if (stringContainsItemFromList(lowerName, new String[]{"shower"}))
            imageId = R.drawable.shower;
        else if (stringContainsItemFromList(lowerName, new String[]{"rak"}))
            imageId = R.drawable.rake;
        else if (stringContainsItemFromList(lowerName, new String[]{"toilet", "washroom", "bathroom"}))
            imageId = R.drawable.toilet;
        else if (stringContainsItemFromList(lowerName, new String[]{"vacuum"}))
            imageId = R.drawable.vacuum;
        else if (stringContainsItemFromList(lowerName, new String[]{"sweep", "broom"}))
            imageId = R.drawable.broom;
        else if (stringContainsItemFromList(lowerName, new String[]{"room", "tidy"}))
            imageId = R.drawable.room;
        else imageId = R.drawable.house;
        return imageId;
    }

    public int getExp() {
        return exp;
    }

    public String getOwner() {
        return owner;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getDescription() {
        return description;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public boolean getRecurring() {
        return recurring;
    }

    public boolean getNeedsVerification() {
        return needsVerification;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getRecurrenceText() {
        int timestamp = getTimestamp();
        switch (timestamp) {
            case 60 * 60 * 24:
                return "Daily";
            case (60 * 60 * 24) * 7:
                return "Weekly";
            case ((60 * 60 * 24) * 7) * 4:
                return "Monthly";
            case (((60 * 60 * 24) * 7) * 4) * 12:
                return "Yearly";
            default:
                return "Only once";
        }
    }

    public int getDueDate() {
        if (recurring) {
            return nextOccurrence;
        } else {
            return dueDate;
        }
    }

    public int getId() {
        return id;
    }

    public String getDueDateString() {
        long currentTime = System.currentTimeMillis();
        long dueDateMilli = (long) getDueDate() * 1000;
        return (String) DateUtils.getRelativeTimeSpanString(dueDateMilli, currentTime, 0L, DateUtils.FORMAT_ABBREV_ALL);
    }

    public QuestCompletion getStatus() {
        long currentTime = System.currentTimeMillis();
        long dueDateMilli = (long) getDueDate() * 1000;
        if ((completedOn > 0 && (!needsVerification) || verifiedOn > 0)) {
            return QuestCompletion.COMPLETED;
        }
        if (completedOn > 0) {
            return QuestCompletion.PENDING;
        }
        if (dueDateMilli > currentTime) {
            return QuestCompletion.UPCOMING;
        }
        return QuestCompletion.OVERDUE;
    }

    public String getQuestCompletionString(QuestCompletion status) {
        switch (status) {
            case OVERDUE:
                return "Overdue";
            case PENDING:
                return "Pending";
            case UPCOMING:
                return "Upcoming";
            default:
                return "Completed";
        }
    }

    @Override
    public int compareTo(QuestRecyclerViewItem otherQuest) {
        if (this.getDueDate() < otherQuest.getDueDate()) {
            return -1;
        }
        if (this.getDueDate() == otherQuest.getDueDate()) {
            return 0;
        }
        return 1;
    }
}
