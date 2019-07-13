package com.chorestory.model;


import com.chorestory.R;
import com.chorestory.templates.ClanChildrenResponse.Children;

import java.util.Random;

public class ChildRecyclerViewItem implements Comparable<ChildRecyclerViewItem>{

    private String name;
    private int level;
    private int exp;
    private int quest;
    private int imageId;

    public ChildRecyclerViewItem(Children child) {
        this.name = child.getName();
        this.level = child.getLevel();
        this.exp = child.getExp();
        this.quest = 10; // TODO: quests completed count somewhere?
        this.imageId = R.drawable.joker_color;// TODO: need to grab this from the endpoint. here maybe?
    }

    @Override
    public int compareTo(ChildRecyclerViewItem otherChild) {
        if (this.level > otherChild.getLevel()) {
            return -1;
        } else if (this.level == otherChild.getLevel() && this.exp >= otherChild.getExp()) {
            return -1;
        } else if (this.level == otherChild.getLevel() && this.exp == otherChild.getExp()) {
            return 0;
        }
        return 1;
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
