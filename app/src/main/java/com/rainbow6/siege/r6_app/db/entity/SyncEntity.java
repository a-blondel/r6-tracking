package com.rainbow6.siege.r6_app.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
public class SyncEntity {

    @PrimaryKey
    @NonNull
    private String profileId;
    private boolean syncProgression;
    private boolean syncEmea;
    private boolean syncNcsa;
    private boolean syncApac;
    private boolean syncGeneral;
    private boolean syncStats;
    private int syncDelay;

    public SyncEntity(@NonNull String profileId, boolean syncProgression, boolean syncEmea, boolean syncNcsa, boolean syncApac, boolean syncGeneral, boolean syncStats, int syncDelay) {
        this.profileId = profileId;
        this.syncProgression = syncProgression;
        this.syncEmea = syncEmea;
        this.syncNcsa = syncNcsa;
        this.syncApac = syncApac;
        this.syncGeneral = syncGeneral;
        this.syncStats = syncStats;
        this.syncDelay = syncDelay;
    }

    @NonNull
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(@NonNull String profileId) {
        this.profileId = profileId;
    }

    public boolean isSyncProgression() {
        return syncProgression;
    }

    public void setSyncProgression(boolean syncProgression) {
        this.syncProgression = syncProgression;
    }

    public boolean isSyncEmea() {
        return syncEmea;
    }

    public void setSyncEmea(boolean syncEmea) {
        this.syncEmea = syncEmea;
    }

    public boolean isSyncNcsa() {
        return syncNcsa;
    }

    public void setSyncNcsa(boolean syncNcsa) {
        this.syncNcsa = syncNcsa;
    }

    public boolean isSyncApac() {
        return syncApac;
    }

    public void setSyncApac(boolean syncApac) {
        this.syncApac = syncApac;
    }

    public boolean isSyncGeneral() {
        return syncGeneral;
    }

    public void setSyncGeneral(boolean syncGeneral) {
        this.syncGeneral = syncGeneral;
    }

    public boolean isSyncStats() {
        return syncStats;
    }

    public void setSyncStats(boolean syncStats) {
        this.syncStats = syncStats;
    }

    public int getSyncDelay() {
        return syncDelay;
    }

    public void setSyncDelay(int syncDelay) {
        this.syncDelay = syncDelay;
    }
}
