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
                R.drawable.king_color,
                R.drawable.queen_color,
                R.drawable.king_color,
                R.drawable.queen_color,
                R.drawable.king_color,
                R.drawable.queen_color};
    }

    private static String[] retrieveNames() { // TODO: get names
        return new String[]{
                "David",
                "Diane",
                "Iulian",
                "Marianne",
                "Bob",
                "Emily"
        };
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
