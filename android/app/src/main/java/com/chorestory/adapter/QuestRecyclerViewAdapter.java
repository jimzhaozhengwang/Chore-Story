package com.chorestory.adapter;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chorestory.R;
import com.chorestory.app.App;
import com.chorestory.app.ChildQuestDetailsActivity;
import com.chorestory.app.ChoreStoryActivity;
import com.chorestory.app.ParentQuestDetailsActivity;
import com.chorestory.helpers.TokenHandler;
import com.chorestory.model.QuestRecyclerViewItem;

import java.util.List;

import javax.inject.Inject;


public class QuestRecyclerViewAdapter extends RecyclerView.Adapter<QuestRecyclerViewAdapter.MyViewHolder> {

    @Inject
    TokenHandler tokenHandler;

    private List<QuestRecyclerViewItem> itemList;
    private ChoreStoryActivity activity;
    private View.OnClickListener onItemClickListener;

    public QuestRecyclerViewAdapter(List<QuestRecyclerViewItem> itemList, ChoreStoryActivity activity) {
        this.itemList = itemList;
        this.activity = activity;
        App.getAppComponent().inject(this);
        onItemClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                QuestRecyclerViewItem currentItem = itemList.get(position);
                int id = currentItem.getId();

                Intent intent;
                String token = tokenHandler.getToken(activity);
                if (tokenHandler.isParentToken(token)) {
                    intent = new Intent(activity, ParentQuestDetailsActivity.class);
                } else {
                    intent = new Intent(activity, ChildQuestDetailsActivity.class);
                }
                intent.putExtra(activity.getResources().getString(R.string.qid), currentItem.getId());
                intent.putExtra(activity.getResources().getString(R.string.owner_name), currentItem.getOwner());
                if (currentItem.getRecurring()) {
                    intent.putExtra(activity.getResources().getString(R.string.ts), currentItem.getDueDate());
                }
                activity.startActivity(intent);
            }
        };
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.quest_recycler_view_item, viewGroup, false);
      
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
        if (currentItem.getOwner() != null) {
            String owner = currentItem.getOwner() + "'s quest";
            myViewHolder.questOwnerTextView.setText(owner);
        }
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
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
            questImageView = itemView.findViewById(R.id.quest_image_view);
            questNameTextView = itemView.findViewById(R.id.quest_name_text_view);
            questOwnerTextView = itemView.findViewById(R.id.quest_owner_text_view);
            questExpTextView = itemView.findViewById(R.id.quest_exp_text_view);
            questDueDateTextView = itemView.findViewById(R.id.quest_due_date_text_view);
        }
    }
}
