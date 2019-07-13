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
import com.chorestory.templates.ClanChildrenResponse.Children;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<ChildRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ChildRecyclerViewItem> childrenList = new ArrayList<>();

    public ChildRecyclerViewAdapter(List<Children> childrenList) {
        // Translate the list of children (in API endpoint format) to list of item view format
        for (Children child : childrenList) {
            this.childrenList.add(new ChildRecyclerViewItem(child));
        }

        // Sort the list of Children in descending order of level and exp
        Collections.sort(this.childrenList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.child_recycler_view_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ChildRecyclerViewItem currChild = childrenList.get(i);
        myViewHolder.childImageView.setImageResource(currChild.getImageId());
        myViewHolder.childNameTextView.setText(currChild.getName());

        String level = "Level " + currChild.getLevel();
        myViewHolder.childLevelTextView.setText(level);

        String exp = currChild.getExp() + " Exp";
        myViewHolder.childExpTextView.setText(exp);

        String quest;
        if (currChild.getQuest() == 1) {
            quest = currChild.getQuest() + " Quest Completed";
        } else {
            quest = currChild.getQuest() + " Quests Completed";
        }

        myViewHolder.childQuestTextView.setText(quest);

        // TODO: handle click events
    }

    @Override
    public int getItemCount() {
        return childrenList.size();
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
