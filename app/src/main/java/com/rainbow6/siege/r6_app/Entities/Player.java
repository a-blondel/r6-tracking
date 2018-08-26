package com.rainbow6.siege.r6_app.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class Player {

    @PrimaryKey
    @NonNull
    private String profileId;
    private String userId;
    private String nameOnPlatform;
    private Date addedDate;

    public Player(String profileId, String userId, String nameOnPlatform, Date addedDate) {
        this.profileId = profileId;
        this.userId = userId;
        this.nameOnPlatform = nameOnPlatform;
        this.addedDate = addedDate;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
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

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
}
