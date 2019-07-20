package com.chorestory.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chorestory.R;
import com.chorestory.app.ChoreStoryActivity;
import com.chorestory.app.ParentQuestDetailsActivity;
import com.chorestory.model.QuestRecyclerViewItem;

import java.util.List;


public class QuestRecyclerViewAdapter extends RecyclerView.Adapter<QuestRecyclerViewAdapter.MyViewHolder> {

    private List<QuestRecyclerViewItem> itemList;
    private ChoreStoryActivity activity;

    public QuestRecyclerViewAdapter(List<QuestRecyclerViewItem> itemList, ChoreStoryActivity activity) {
        this.itemList = itemList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.quest_recycler_view_item, viewGroup, false);

        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                QuestRecyclerViewItem currentItem = itemList.get(i);
                System.out.println("FIND I: " + i);
                Intent intent = new Intent(activity, ParentQuestDetailsActivity.class);
                System.out.println("QID1: " + currentItem.getId());
                intent.putExtra("qid", currentItem.getId());
                intent.putExtra("ownerName", currentItem.getOwner());
                activity.startActivity(intent);
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        QuestRecyclerViewItem currentItem = itemList.get(i);

        // Quest icon
        myViewHolder.questImageView.setImageResource(currentItem.getImageId());
        // Quest name
        myViewHolder.questNameTextView.setText(currentItem.getName());
        // Quest owner
        String owner = currentItem.getOwner() + "'s quest";
        myViewHolder.questOwnerTextView.setText(owner);
        // Quest Exp
        String exp = currentItem.getExp() + " Exp";
        myViewHolder.questExpTextView.setText(exp);
        // Quest due date
        myViewHolder.questDueDateTextView.setText("Due " + currentItem.getDueDateString());
        // TODO show different things for completed quests
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView questImageView;
        private TextView questNameTextView;
        private TextView questOwnerTextView;
        private TextView questExpTextView;
        private TextView questDueDateTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            questImageView = itemView.findViewById(R.id.quest_image_view);
            questNameTextView = itemView.findViewById(R.id.quest_name_text_view);
            questOwnerTextView = itemView.findViewById(R.id.quest_owner_text_view);
            questExpTextView = itemView.findViewById(R.id.quest_exp_text_view);
            questDueDateTextView = itemView.findViewById(R.id.quest_due_date_text_view);
        }
    }
}
