package com.rainbow6.siege.r6_app.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
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
        )},
        indices = {@Index("profileId")}
        )
public class General {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "profileId")
    private String profileId;
    private Date updateDate;
    private int timePlayed;
    private int matchWon;
    private int matchLost;
    private int kills;
    private int death;
    private int headshots;
    private int bulletHit;
    private int bulletFired;
    private int killAssists;

    public General(@NonNull int id, String profileId, Date updateDate, int timePlayed, int matchWon, int matchLost, int kills, int death, int headshots, int bulletHit, int bulletFired, int killAssists) {
        this.id = id;
        this.profileId = profileId;
        this.updateDate = updateDate;
        this.timePlayed = timePlayed;
        this.matchWon = matchWon;
        this.matchLost = matchLost;
        this.kills = kills;
        this.death = death;
        this.headshots = headshots;
        this.bulletHit = bulletHit;
        this.bulletFired = bulletFired;
        this.killAssists = killAssists;
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

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public int getMatchWon() {
        return matchWon;
    }

    public void setMatchWon(int matchWon) {
        this.matchWon = matchWon;
    }

    public int getMatchLost() {
        return matchLost;
    }

    public void setMatchLost(int matchLost) {
        this.matchLost = matchLost;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public int getHeadshots() {
        return headshots;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public int getBulletHit() {
        return bulletHit;
    }

    public void setBulletHit(int bulletHit) {
        this.bulletHit = bulletHit;
    }

    public int getBulletFired() {
        return bulletFired;
    }

    public void setBulletFired(int bulletFired) {
        this.bulletFired = bulletFired;
    }

    public int getKillAssists() {
        return killAssists;
    }

    public void setKillAssists(int killAssists) {
        this.killAssists = killAssists;
    }
}
