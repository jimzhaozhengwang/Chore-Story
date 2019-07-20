package com.chorestory.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.model.ParentRecyclerViewItem;
import com.chorestory.templates.ClanResponse.Data.Parent;

import java.util.ArrayList;
import java.util.List;

public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<ParentRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ParentRecyclerViewItem> parentList = new ArrayList<ParentRecyclerViewItem>();

    public ParentRecyclerViewAdapter(List<Parent> parentList) {

        for (Parent parent : parentList) {
            this.parentList.add(new ParentRecyclerViewItem(parent));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.parent_recycler_view_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ParentRecyclerViewItem currentItem = parentList.get(i);
        myViewHolder.parentImageView.setImageResource(currentItem.getImageId());
        myViewHolder.parentNameTextView.setText(currentItem.getName());

        // TODO: handle click events
    }

    @Override
    public int getItemCount() {
        return parentList.size();
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
