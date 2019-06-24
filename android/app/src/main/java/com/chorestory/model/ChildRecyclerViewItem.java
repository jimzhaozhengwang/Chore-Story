package com.chorestory.model;

import com.chorestory.R;

import java.util.ArrayList;
import java.util.List;

public class ChildRecyclerViewItem {

    private String name;
    private int imageId;
    private int level;
    private int exp;
    private int quest;

    public static List<ChildRecyclerViewItem> getData() {
        List<ChildRecyclerViewItem> dataList = new ArrayList<>(); // TODO: sort decreasing exp
        String[] names = retrieveNames();
        int[] imageIds = retrieveImages();
        int[] levels = retrieveLevels();
        int[] exps = retrieveExps();
        int[] quests = retrieveQuests();

        for (int i = 0; i < names.length; ++i) {
            ChildRecyclerViewItem item = new ChildRecyclerViewItem();
            item.name = names[i];
            item.imageId = imageIds[i];
            item.level = levels[i];
            item.exp = exps[i];
            item.quest = quests[i];
            dataList.add(item);
        }
        return dataList;
    }

    private static int[] retrieveImages() { // TODO: retrieve images
        return new int[]{
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground};
    }

    private static String[] retrieveNames() { // TODO: retrieve names
        return new String[]{
                "Child 1",
                "Child 2",
                "Child 3",
                "Child 4",
                "Child 5",
                "Child 6"
        };
    }

    private static int[] retrieveLevels() { // TODO: retrieve levels
        return new int[]{
                4, 1, 10, 2, 4, 9
        };
    }

    private static int[] retrieveExps() { // TODO: retrieve exps
        return new int[]{
                400, 12, 10021, 200, 442, 3919
        };
    }

    private static int[] retrieveQuests() {
        return new int[]{
                40, 1, 200, 20, 42, 100
        };
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
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
}
