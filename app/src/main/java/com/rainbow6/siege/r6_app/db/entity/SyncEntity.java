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
    private boolean syncStats;
    private int syncDelay;
    private long lastSync;

    public SyncEntity(@NonNull String profileId, boolean syncProgression, boolean syncEmea, boolean syncNcsa, boolean syncApac, boolean syncStats, int syncDelay, long lastSync) {
        this.profileId = profileId;
        this.syncProgression = syncProgression;
        this.syncEmea = syncEmea;
        this.syncNcsa = syncNcsa;
        this.syncApac = syncApac;
        this.syncStats = syncStats;
        this.syncDelay = syncDelay;
        this.lastSync = lastSync;
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

    public long getLastSync() { return lastSync; }

    public void setLastSync(long lastSync) { this.lastSync = lastSync; }
}
