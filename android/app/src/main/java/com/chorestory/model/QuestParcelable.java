package com.chorestory.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.chorestory.templates.GetQuestsResponse;


public class QuestParcelable implements Parcelable {

    String completedOn;
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
    String verifiedOn;

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
        this.ownerName = quest.getOwner().getName();
        this.ownerId = quest.getOwner().getId();
        this.recurring = quest.getRecurring();
        this.reward = quest.getReward();
        this.title = quest.getTitle();
        this.verifiedOn = quest.getVerifiedOn();
    }

    public QuestParcelable(Parcel source) {
        completedOn = source.readString();
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
        verifiedOn = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(completedOn);
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
        dest.writeString(verifiedOn);
    }

    public String getCompletedOn() {
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
        return  ownerName;
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

    public String getVerifiedOn() {
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
