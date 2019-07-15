package com.chorestory.model;

import com.chorestory.R;
import com.chorestory.helpers.QuestCompletion;

import java.util.ArrayList;
import java.util.List;

public class QuestRecyclerViewItem {

    private String name;
    private int imageId;
    private int exp;
    private String owner;
    private String description;
    private boolean mandatory;
    private int dueDate;

    public static List<QuestRecyclerViewItem> getData(QuestCompletion questCompletion) {
        List<QuestRecyclerViewItem> dataList = new ArrayList<>();
        // TODO: fetch quests using QuestCompletion
        for (int i = 0; i < 2; ++i) {
            QuestRecyclerViewItem item = new QuestRecyclerViewItem("Sweep the floor", R.drawable.broom, 30, "Isabelle", "sweep it for 10 hours plz", true, 5);
            dataList.add(item);
        }
        return dataList;
    }

    QuestRecyclerViewItem(String name, int imageId, int exp, String owner, String description, boolean mandatory, int dueDate) {
        this.name = name;
        this.imageId = imageId;
        this.exp = exp;
        this.owner = owner;
        this.description = description;
        this.mandatory = mandatory;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
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

}
