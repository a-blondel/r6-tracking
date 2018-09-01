package com.rainbow6.siege.r6_app.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = PlayerEntity.class,
                parentColumns = "profileId",
                childColumns = "profileId",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )},
        indices = {@Index("profileId")}
        )
public class ProgressionEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String profileId;
    private Date updateDate;
    private int xp;
    private int level;
    private int lootChance;

    public ProgressionEntity(int id, String profileId, Date updateDate, int xp, int level, int lootChance) {
        this.id = id;
        this.profileId = profileId;
        this.updateDate = updateDate;
        this.xp = xp;
        this.level = level;
        this.lootChance = lootChance;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
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

    public int getXp() { return xp; }

    public void setXp(int xp) { this.xp = xp; }

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
