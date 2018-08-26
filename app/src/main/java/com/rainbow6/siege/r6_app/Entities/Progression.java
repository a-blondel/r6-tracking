package com.rainbow6.siege.r6_app.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Player.class,
                parentColumns = "profileId",
                childColumns = "profileId",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )}
        )
public class Progression {

    @PrimaryKey
    @NonNull
    private String profileId;
    private Date updateDate;
    private int level;
    private int lootChance;

    public Progression(String profileId, Date updateDate, int level, int lootChance) {
        this.profileId = profileId;
        this.updateDate = updateDate;
        this.level = level;
        this.lootChance = lootChance;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLootChance() {
        return lootChance;
    }

    public void setLootChance(int lootChance) {
        this.lootChance = lootChance;
    }
}
