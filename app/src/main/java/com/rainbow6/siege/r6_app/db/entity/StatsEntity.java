package com.rainbow6.siege.r6_app.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
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
public class StatsEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String profileId;
    private Date updateDate;
    private int timePlayedRanked;
    private int matchPlayedRanked;
    private int matchWonRanked;
    private int matchLostRanked;
    private int killsRanked;
    private int deathRanked;
    private int timePlayedCasual;
    private int matchPlayedCasual;
    private int matchWonCasual;
    private int matchLostCasual;
    private int killsCasual;
    private int deathCasual;
    private int generalTimePlayed;
    private int generalMatchPlayed;
    private int generalMatchWon;
    private int generalMatchLost;
    private int generalKills;
    private int generalDeath;
    private int generalHeadshots;
    private int generalBulletHit;
    private int generalBulletFired;
    private int generalKillAssists;
    private int generalPenetrationKills;
    private int generalMeleeKills;
    private int generalRevive;

    public StatsEntity(@NonNull int id, String profileId, Date updateDate, int timePlayedRanked, int matchPlayedRanked, int matchWonRanked, int matchLostRanked, int killsRanked, int deathRanked, int timePlayedCasual, int matchPlayedCasual, int matchWonCasual, int matchLostCasual, int killsCasual, int deathCasual, int generalTimePlayed, int generalMatchPlayed, int generalMatchWon, int generalMatchLost, int generalKills, int generalDeath, int generalHeadshots, int generalBulletHit, int generalBulletFired, int generalKillAssists, int generalPenetrationKills, int generalMeleeKills, int generalRevive) {
        this.id = id;
        this.profileId = profileId;
        this.updateDate = updateDate;
        this.timePlayedRanked = timePlayedRanked;
        this.matchPlayedRanked = matchPlayedRanked;
        this.matchWonRanked = matchWonRanked;
        this.matchLostRanked = matchLostRanked;
        this.killsRanked = killsRanked;
        this.deathRanked = deathRanked;
        this.timePlayedCasual = timePlayedCasual;
        this.matchPlayedCasual = matchPlayedCasual;
        this.matchWonCasual = matchWonCasual;
        this.matchLostCasual = matchLostCasual;
        this.killsCasual = killsCasual;
        this.deathCasual = deathCasual;
        this.generalTimePlayed = generalTimePlayed;
        this.generalMatchPlayed = generalMatchPlayed;
        this.generalMatchWon = generalMatchWon;
        this.generalMatchLost = generalMatchLost;
        this.generalKills = generalKills;
        this.generalDeath = generalDeath;
        this.generalHeadshots = generalHeadshots;
        this.generalBulletHit = generalBulletHit;
        this.generalBulletFired = generalBulletFired;
        this.generalKillAssists = generalKillAssists;
        this.generalPenetrationKills = generalPenetrationKills;
        this.generalMeleeKills = generalMeleeKills;
        this.generalRevive = generalRevive;
    }

    @Ignore
    public StatsEntity() {
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

    public int getTimePlayedRanked() {
        return timePlayedRanked;
    }

    public void setTimePlayedRanked(int timePlayedRanked) {
        this.timePlayedRanked = timePlayedRanked;
    }

    public int getMatchPlayedRanked() {
        return matchPlayedRanked;
    }

    public void setMatchPlayedRanked(int matchPlayedRanked) {
        this.matchPlayedRanked = matchPlayedRanked;
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

    public int getMatchPlayedCasual() {
        return matchPlayedCasual;
    }

    public void setMatchPlayedCasual(int matchPlayedCasual) {
        this.matchPlayedCasual = matchPlayedCasual;
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

    public int getGeneralTimePlayed() {
        return generalTimePlayed;
    }

    public void setGeneralTimePlayed(int generalTimePlayed) {
        this.generalTimePlayed = generalTimePlayed;
    }

    public int getGeneralMatchPlayed() {
        return generalMatchPlayed;
    }

    public void setGeneralMatchPlayed(int generalMatchPlayed) {
        this.generalMatchPlayed = generalMatchPlayed;
    }

    public int getGeneralMatchWon() {
        return generalMatchWon;
    }

    public void setGeneralMatchWon(int generalMatchWon) {
        this.generalMatchWon = generalMatchWon;
    }

    public int getGeneralMatchLost() {
        return generalMatchLost;
    }

    public void setGeneralMatchLost(int generalMatchLost) {
        this.generalMatchLost = generalMatchLost;
    }

    public int getGeneralKills() {
        return generalKills;
    }

    public void setGeneralKills(int generalKills) {
        this.generalKills = generalKills;
    }

    public int getGeneralDeath() {
        return generalDeath;
    }

    public void setGeneralDeath(int generalDeath) {
        this.generalDeath = generalDeath;
    }

    public int getGeneralHeadshots() {
        return generalHeadshots;
    }

    public void setGeneralHeadshots(int generalHeadshots) {
        this.generalHeadshots = generalHeadshots;
    }

    public int getGeneralBulletHit() {
        return generalBulletHit;
    }

    public void setGeneralBulletHit(int generalBulletHit) {
        this.generalBulletHit = generalBulletHit;
    }

    public int getGeneralBulletFired() {
        return generalBulletFired;
    }

    public void setGeneralBulletFired(int generalBulletFired) {
        this.generalBulletFired = generalBulletFired;
    }

    public int getGeneralKillAssists() {
        return generalKillAssists;
    }

    public void setGeneralKillAssists(int generalKillAssists) {
        this.generalKillAssists = generalKillAssists;
    }

    public int getGeneralPenetrationKills() {
        return generalPenetrationKills;
    }

    public void setGeneralPenetrationKills(int generalPenetrationKills) {
        this.generalPenetrationKills = generalPenetrationKills;
    }

    public int getGeneralMeleeKills() {
        return generalMeleeKills;
    }

    public void setGeneralMeleeKills(int generalMeleeKills) {
        this.generalMeleeKills = generalMeleeKills;
    }

    public int getGeneralRevive() {
        return generalRevive;
    }

    public void setGeneralRevive(int generalRevive) {
        this.generalRevive = generalRevive;
    }
}
