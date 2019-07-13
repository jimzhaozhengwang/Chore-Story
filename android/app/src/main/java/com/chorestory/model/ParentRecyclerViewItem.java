package com.chorestory.model;

import com.chorestory.R;
import com.chorestory.templates.ClanResponse.Data.Parent;

public class ParentRecyclerViewItem {

    private String name;
    private int imageId;

    public ParentRecyclerViewItem(Parent parent) {
        this.name = parent.getName();
        this.imageId = R.drawable.king_color; // TODO: use this when the response is ready: parent.getPicture();
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
