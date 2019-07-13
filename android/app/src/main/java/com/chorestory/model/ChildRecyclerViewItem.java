package com.chorestory.model;


import com.chorestory.R;
import com.chorestory.templates.ClanChildrenResponse.Children;

public class ChildRecyclerViewItem {

    private String name;
    private int level;
    private int exp;
    private int quest;
    private int imageId;

    public ChildRecyclerViewItem(Children child) {
        this.name = child.getName();
        this.level = child.getLevel();
        this.exp = child.getXp();
        this.quest = 10; // TODO: quests completed count somewhere?
        this.imageId = R.drawable.joker_color;// TODO: need to set this somewhere
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getQuest() {
        return quest;
    }

    public int getImageId() {
        return imageId;
    }
}
