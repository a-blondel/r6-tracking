package com.rainbow6.siege.r6_app.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class PlayerEntity {

    @PrimaryKey
    @NonNull
    private String profileId;
    private String userId;
    private String nameOnPlatform;
    private String platformType;
    private Date addedDate;

    public PlayerEntity(@NonNull String profileId, String userId, String nameOnPlatform, String platformType, Date addedDate) {
        this.profileId = profileId;
        this.userId = userId;
        this.nameOnPlatform = nameOnPlatform;
        this.platformType = platformType;
        this.addedDate = addedDate;
    }

    @NonNull
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(@NonNull String profileId) {
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNameOnPlatform() {
        return nameOnPlatform;
    }

    public void setNameOnPlatform(String nameOnPlatform) {
        this.nameOnPlatform = nameOnPlatform;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
}
