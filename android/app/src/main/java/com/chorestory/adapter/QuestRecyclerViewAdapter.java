package com.chorestory.adapter;

import androidx.recyclerview.widget.RecyclerView;
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
                activity.navigateTo(ParentQuestDetailsActivity.class);
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
        // Quest mandatory/optional
        String mandatory;
        if (currentItem.getMandatory()) {
            mandatory = activity.getString(R.string.mandatory);
        } else {
            mandatory = activity.getString(R.string.optional);
        }
        myViewHolder.questMandatoryTextView.setText(mandatory);
        // Quest Exp
        String exp = currentItem.getExp() + " Exp";
        myViewHolder.questExpTextView.setText(exp);
        // Quest due date
        // TODO
        String dueDate = "Due in " + Integer.toString(currentItem.getDueDate()) + " days";
        myViewHolder.questDueDateTextView.setText(dueDate);

        // TODO: handle click events
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
        private TextView questMandatoryTextView;
        private TextView questDueDateTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            questImageView = itemView.findViewById(R.id.quest_image_view);
            questNameTextView = itemView.findViewById(R.id.quest_name_text_view);
            questOwnerTextView = itemView.findViewById(R.id.quest_owner_text_view);
            questExpTextView = itemView.findViewById(R.id.quest_exp_text_view);
            questMandatoryTextView = itemView.findViewById(R.id.quest_mandatory_text_view);
            questDueDateTextView = itemView.findViewById(R.id.quest_due_date_text_view);
        }
    }
}
