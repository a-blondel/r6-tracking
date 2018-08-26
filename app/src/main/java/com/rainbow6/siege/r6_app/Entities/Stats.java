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
public class Stats {

    @PrimaryKey
    @NonNull
    private String profileId;
    private Date updateDate;
    private int timePlayedRanked;
    private int matchWonRanked;
    private int matchLostRanked;
    private int killsRanked;
    private int deathRanked;
    private int timePlayedCasual;
    private int matchWonCasual;
    private int matchLostCasual;
    private int killsCasual;
    private int deathCasual;

    public Stats(String profileId, Date updateDate, int timePlayedRanked, int matchWonRanked, int matchLostRanked, int killsRanked, int deathRanked, int timePlayedCasual, int matchWonCasual, int matchLostCasual, int killsCasual, int deathCasual) {
        this.profileId = profileId;
        this.updateDate = updateDate;
        this.timePlayedRanked = timePlayedRanked;
        this.matchWonRanked = matchWonRanked;
        this.matchLostRanked = matchLostRanked;
        this.killsRanked = killsRanked;
        this.deathRanked = deathRanked;
        this.timePlayedCasual = timePlayedCasual;
        this.matchWonCasual = matchWonCasual;
        this.matchLostCasual = matchLostCasual;
        this.killsCasual = killsCasual;
        this.deathCasual = deathCasual;
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

    public int getTimePlayedRanked() {
        return timePlayedRanked;
    }

    public void setTimePlayedRanked(int timePlayedRanked) {
        this.timePlayedRanked = timePlayedRanked;
    }

    public int getMatchWonRanked() {
        return matchWonRanked;
    }

    public void setMatchWonRanked(int matchWonRanked) {
        this.matchWonRanked = matchWonRanked;
    }

    public int getMatchLostRanked() {
        return matchLostRanked;
    }

    public void setMatchLostRanked(int matchLostRanked) {
        this.matchLostRanked = matchLostRanked;
    }

    public int getKillsRanked() {
        return killsRanked;
    }

    public void setKillsRanked(int killsRanked) {
        this.killsRanked = killsRanked;
    }

    public int getDeathRanked() {
        return deathRanked;
    }

    public void setDeathRanked(int deathRanked) {
        this.deathRanked = deathRanked;
    }

    public int getTimePlayedCasual() {
        return timePlayedCasual;
    }

    public void setTimePlayedCasual(int timePlayedCasual) {
        this.timePlayedCasual = timePlayedCasual;
    }

    public int getMatchWonCasual() {
        return matchWonCasual;
    }

    public void setMatchWonCasual(int matchWonCasual) {
        this.matchWonCasual = matchWonCasual;
    }

    public int getMatchLostCasual() {
        return matchLostCasual;
    }

    public void setMatchLostCasual(int matchLostCasual) {
        this.matchLostCasual = matchLostCasual;
    }

    public int getKillsCasual() {
        return killsCasual;
    }

    public void setKillsCasual(int killsCasual) {
        this.killsCasual = killsCasual;
    }

    public int getDeathCasual() {
        return deathCasual;
    }

    public void setDeathCasual(int deathCasual) {
        this.deathCasual = deathCasual;
    }
}
