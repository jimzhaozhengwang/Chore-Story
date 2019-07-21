package com.chorestory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chorestory.helpers.QuestCompletion;
import com.chorestory.templates.GetQuestsResponse;


public class QuestParcelable implements Parcelable {

    int completedOn;
    String description;
    int due;
    int id;
    boolean needsVerification;
    int nextOccurrence;
    String ownerName;
    int ownerId;
    boolean recurring;
    int reward;
    String title;
    int verifiedOn;

    public QuestParcelable(GetQuestsResponse.Quest quest) {
        this.completedOn = quest.getCompletedOn();
        this.description = quest.getDescription();
        this.due = quest.getDue();
        this.id = quest.getId();
        this.needsVerification = quest.getNeedsVerification();
        if (quest.getRecurring()) {
            this.nextOccurrence = quest.getNextOccurrence();
        } else { // nextOccurrence is null so add dummy value
            this.nextOccurrence = 0;
        }
        if (quest.getOwner() != null) {
            this.ownerName = quest.getOwner().getName();
            this.ownerId = quest.getOwner().getId();
        } else {
            this.ownerName = null;
            this.ownerId = -1;
        }
        this.recurring = quest.getRecurring();
        this.reward = quest.getReward();
        this.title = quest.getTitle();
        this.verifiedOn = quest.getVerifiedOn();
    }

    public QuestParcelable(Parcel source) {
        completedOn = source.readInt();
        description = source.readString();
        due = source.readInt();
        id = source.readInt();
        needsVerification = source.readByte() != 0;
        nextOccurrence = source.readInt();
        ownerName = source.readString();
        ownerId = source.readInt();
        recurring = source.readByte() != 0;
        reward = source.readInt();
        title = source.readString();
        verifiedOn = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(completedOn);
        dest.writeString(description);
        dest.writeInt(due);
        dest.writeInt(id);
        dest.writeByte((byte) (needsVerification ? 1 : 0));
        dest.writeInt(nextOccurrence);
        dest.writeString(ownerName);
        dest.writeInt(ownerId);
        dest.writeByte((byte) (recurring ? 1 : 0));
        dest.writeInt(reward);
        dest.writeString(title);
        dest.writeInt(verifiedOn);
    }

    public int getCompletedOn() {
        return completedOn;
    }

    public String getDescription() {
        return description;
    }

    public int getDue() {
        return due;
    }

    public int getId() {
        return id;
    }

    public boolean getNeedsVerification() {
        return needsVerification;
    }

    public int getNextOccurrence() {
        return nextOccurrence;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public boolean getReccurring() {
        return recurring;
    }

    public int getReward() {
        return reward;
    }

    public String getTitle() {
        return title;
    }

    public int getVerifiedOn() {
        return verifiedOn;
    }

    public static final Creator<QuestParcelable> CREATOR = new Creator<QuestParcelable>() {
        @Override
        public QuestParcelable[] newArray(int size) {
            return new QuestParcelable[size];
        }

        @Override
        public QuestParcelable createFromParcel(Parcel source) {
            return new QuestParcelable(source);
        }
    };
}
