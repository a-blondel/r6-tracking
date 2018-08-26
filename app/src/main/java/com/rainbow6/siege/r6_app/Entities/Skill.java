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
public class Skill {

    @PrimaryKey
    @NonNull
    private String profileId;
    private Date updateDate;
    private Double skillMean;
    private int wins;
    private int losses;
    private int abandons;
    private int season;
    private String region;
    private Double maxMmr;
    private Double mmr;
    private int previousRankMmr;
    private int nextRankMmr;
    private int rank;
    private int maxRank;

    public Skill(String profileId, Date updateDate, Double skillMean, int wins, int losses, int abandons, int season, String region, Double maxMmr, Double mmr, int previousRankMmr, int nextRankMmr, int rank, int maxRank) {
        this.profileId = profileId;
        this.updateDate = updateDate;
        this.skillMean = skillMean;
        this.wins = wins;
        this.losses = losses;
        this.abandons = abandons;
        this.season = season;
        this.region = region;
        this.maxMmr = maxMmr;
        this.mmr = mmr;
        this.previousRankMmr = previousRankMmr;
        this.nextRankMmr = nextRankMmr;
        this.rank = rank;
        this.maxRank = maxRank;
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

    public Double getSkillMean() {
        return skillMean;
    }

    public void setSkillMean(Double skillMean) {
        this.skillMean = skillMean;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getAbandons() {
        return abandons;
    }

    public void setAbandons(int abandons) {
        this.abandons = abandons;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getMaxMmr() {
        return maxMmr;
    }

    public void setMaxMmr(Double maxMmr) {
        this.maxMmr = maxMmr;
    }

    public Double getMmr() {
        return mmr;
    }

    public void setMmr(Double mmr) {
        this.mmr = mmr;
    }

    public int getPreviousRankMmr() {
        return previousRankMmr;
    }

    public void setPreviousRankMmr(int previousRankMmr) {
        this.previousRankMmr = previousRankMmr;
    }

    public int getNextRankMmr() {
        return nextRankMmr;
    }

    public void setNextRankMmr(int nextRankMmr) {
        this.nextRankMmr = nextRankMmr;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMaxRank() {
        return maxRank;
    }

    public void setMaxRank(int maxRank) {
        this.maxRank = maxRank;
    }
}
