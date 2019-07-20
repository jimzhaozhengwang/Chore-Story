package com.chorestory.model;

import android.text.format.DateUtils;

import com.chorestory.R;
import com.chorestory.helpers.QuestCompletion;

import java.util.ArrayList;
import java.util.List;

public class QuestRecyclerViewItem {

    private String name;
    private int exp;
    private String owner;
    private String description;
    private boolean mandatory;
    private int dueDate;
    private boolean recurring;
    private int nextOccurrence;

    public static List<QuestRecyclerViewItem> getData(ArrayList<QuestParcelable> questParcelables) {
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
        this.description = quest.getDescription();
        this.mandatory = true;
        this.dueDate = quest.getDue();
        this.recurring = quest.getReccurring();
        this.nextOccurrence = quest.getNextOccurrence();
    }

    public String getName() {
        return name;
    }

    private boolean stringContainsItemFromList(String inputStr, String[] items) {
        for(int i =0; i < items.length; i++) {
            if (inputStr.contains(items[i])) {
                return true;
            }
        }
        return false;
    }

    public int getImageId() {
        String lowerName = name.toLowerCase();
        int imageId;
        if (stringContainsItemFromList(lowerName, new String[]{"laundry", "washer", "dryer"})) imageId = R.drawable.washing_machine;
        else if (stringContainsItemFromList(lowerName, new String[]{"fold"})) imageId = R.drawable.laundry;
        else if (stringContainsItemFromList(lowerName, new String[]{"dish"})) imageId = R.drawable.washing_plate;
        else if (stringContainsItemFromList(lowerName, new String[]{"meal", "food"})) imageId = R.drawable.dish;
        else if (stringContainsItemFromList(lowerName, new String[]{"dust"})) imageId = R.drawable.dusting;
        else if (stringContainsItemFromList(lowerName, new String[]{"mop"})) imageId = R.drawable.floor_mop;
        else if (stringContainsItemFromList(lowerName, new String[]{"paint"})) imageId = R.drawable.paint;
        else if (stringContainsItemFromList(lowerName, new String[]{"garbage", "trash", "rubbish", "recycl", "throw"})) imageId = R.drawable.garbage;
        else if (stringContainsItemFromList(lowerName, new String[]{"plant", "garden"})) imageId = R.drawable.garden_hose;
        else if (stringContainsItemFromList(lowerName, new String[]{"iron", "press"})) imageId = R.drawable.iron;
        else if (stringContainsItemFromList(lowerName, new String[]{"lawn", "grass", "mow"})) imageId = R.drawable.lawn_mower;
        else if (stringContainsItemFromList(lowerName, new String[]{"paint"})) imageId = R.drawable.paint;
        else if (stringContainsItemFromList(lowerName, new String[]{"shower, teeth"})) imageId = R.drawable.shower;
        else if (stringContainsItemFromList(lowerName, new String[]{"rak"})) imageId = R.drawable.rake;
        else if (stringContainsItemFromList(lowerName, new String[]{"toilet", "washroom", "bathroom"})) imageId = R.drawable.toilet;
        else if (stringContainsItemFromList(lowerName, new String[]{"vacuum"})) imageId = R.drawable.vacuum;
        else if (stringContainsItemFromList(lowerName, new String[]{"sweep", "broom"})) imageId = R.drawable.broom;
        else if (stringContainsItemFromList(lowerName, new String[]{"room", "tidy"})) imageId = R.drawable.room;
        else imageId = R.drawable.house;
        return imageId;
    }

    public int getExp() {
        return exp;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public int getDueDate() {
        return dueDate;
    }

    public String getDueDateString() {
        long currentTime = System.currentTimeMillis();
        long dueDateMilli;
        if (recurring) {
            dueDateMilli = ((long) nextOccurrence) * 1000;
        } else{
            dueDateMilli = ((long) dueDate) * 1000;
        }
        return (String) DateUtils.getRelativeTimeSpanString(dueDateMilli, currentTime, 0L, DateUtils.FORMAT_ABBREV_ALL);
    }
}
