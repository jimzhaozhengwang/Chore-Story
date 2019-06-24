package com.chorestory.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.model.ParentRecyclerViewItem;

import java.util.List;

public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<ParentRecyclerViewAdapter.MyViewHolder> {

    private List<ParentRecyclerViewItem> itemList;

    public ParentRecyclerViewAdapter(List<ParentRecyclerViewItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.parent_recycler_view_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ParentRecyclerViewItem currentItem = itemList.get(i);
        myViewHolder.parentImageView.setImageResource(currentItem.getImageId());
        myViewHolder.parentNameTextView.setText(currentItem.getName());

        // TODO: handle click events
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView parentImageView;
        private TextView parentNameTextView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentImageView = itemView.findViewById(R.id.parent_image_view);
            parentNameTextView = itemView.findViewById(R.id.parent_name_text_view);
        }
    }
}
