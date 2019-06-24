package com.chorestory.model;

import com.chorestory.R;

import java.util.ArrayList;
import java.util.List;

public class ParentRecyclerViewItem {

    private String name;
    private int imageId;

    public static List<ParentRecyclerViewItem> getData() {
        List<ParentRecyclerViewItem> dataList = new ArrayList<>();
        String[] names = retrieveNames();
        int[] imageIds = retrieveImages();

        for (int i = 0; i < names.length; ++i) {
            ParentRecyclerViewItem item = new ParentRecyclerViewItem();
            item.name = names[i];
            item.imageId = imageIds[i];
            dataList.add(item);
        }
        return dataList;
    }

    private static int[] retrieveImages() { // TODO: get images
        return new int[]{
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground};
    }

    private static String[] retrieveNames() { // TODO: get names
        return new String[]{
                "Parent 1",
                "Guardian 1",
                "Parent 2",
                "Guardian 2",
                "Guardian 3",
                "Guardian 4"
        };
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
