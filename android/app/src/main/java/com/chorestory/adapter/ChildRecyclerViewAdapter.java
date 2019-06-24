package com.chorestory.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.model.ChildRecyclerViewItem;

import java.util.List;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<ChildRecyclerViewAdapter.MyViewHolder> {

    private List<ChildRecyclerViewItem> itemList;

    public ChildRecyclerViewAdapter(List<ChildRecyclerViewItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.child_recycler_view_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ChildRecyclerViewItem currentItem = itemList.get(i);
        myViewHolder.childImageView.setImageResource(currentItem.getImageId());
        myViewHolder.childNameTextView.setText(currentItem.getName());

        String level = "Level " + currentItem.getLevel();
        myViewHolder.childLevelTextView.setText(level);

        String exp = currentItem.getExp() + " Exp";
        myViewHolder.childExpTextView.setText(exp);

        String quest;
        if (currentItem.getQuest() == 1) {
            quest = currentItem.getQuest() + " Quest Completed";
        } else {
            quest = currentItem.getQuest() + " Quests Completed";
        }

        myViewHolder.childQuestTextView.setText(quest);

        // TODO: handle click events
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView childImageView;
        private TextView childNameTextView;
        private TextView childLevelTextView;
        private TextView childExpTextView;
        private TextView childQuestTextView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            childImageView = itemView.findViewById(R.id.child_image_view);
            childNameTextView = itemView.findViewById(R.id.child_name_text_view);
            childLevelTextView = itemView.findViewById(R.id.child_level_text_view);
            childExpTextView = itemView.findViewById(R.id.child_exp_text_view);
            childQuestTextView = itemView.findViewById(R.id.child_quest_text_view);
        }
    }
}
