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
    private int generalPvpBlindKills;
    private int generalPvpRappelBreach;
    private int generalPvpDbno;
    private int generalPvpDbnoAssists;
    private int generalPvpSuicide;
    private int generalPvpBarricadeDeployed;
    private int generalPvpReinforcementDeploy;
    private int generalPvpDistanceTravelled;
    private int generalPvpReviveDenied;
    private int generalPvpGadgetDestroy;
    private int generalPvpTotalXp;
    private int secureareapvpTimeplayed;
    private int rescuehostagepvpTimeplayed;
    private int plantbombpvpTimeplayed;
    private int secureareapvpMatchwon;
    private int secureareapvpMatchlost;
    private int secureareapvpMatchplayed;
    private int secureareapvpBestscore;
    private int rescuehostagepvpMatchwon;
    private int rescuehostagepvpMatchlost;
    private int rescuehostagepvpMatchplayed;
    private int rescuehostagepvpBestscore;
    private int plantbombpvpMatchwon;
    private int plantbombpvpMatchlost;
    private int plantbombpvpMatchplayed;
    private int plantbombpvpBestscore;
    private int generalpvpServershacked;
    private int generalpvpServerdefender;
    private int generalpvpServeraggression;
    private int generalpvpHostagerescue;
    private int generalpvpHostagedefense;

    public StatsEntity(@NonNull int id, String profileId, Date updateDate, int timePlayedRanked, int matchPlayedRanked, int matchWonRanked, int matchLostRanked, int killsRanked, int deathRanked, int timePlayedCasual, int matchPlayedCasual, int matchWonCasual, int matchLostCasual, int killsCasual, int deathCasual, int generalTimePlayed, int generalMatchPlayed, int generalMatchWon, int generalMatchLost, int generalKills, int generalDeath, int generalHeadshots, int generalBulletHit, int generalBulletFired, int generalKillAssists, int generalPenetrationKills, int generalMeleeKills, int generalRevive, int generalPvpBlindKills, int generalPvpRappelBreach, int generalPvpDbno, int generalPvpDbnoAssists, int generalPvpSuicide, int generalPvpBarricadeDeployed, int generalPvpReinforcementDeploy, int generalPvpDistanceTravelled, int generalPvpReviveDenied, int generalPvpGadgetDestroy, int generalPvpTotalXp, int secureareapvpTimeplayed, int rescuehostagepvpTimeplayed, int plantbombpvpTimeplayed, int secureareapvpMatchwon, int secureareapvpMatchlost, int secureareapvpMatchplayed, int secureareapvpBestscore, int rescuehostagepvpMatchwon, int rescuehostagepvpMatchlost, int rescuehostagepvpMatchplayed, int rescuehostagepvpBestscore, int plantbombpvpMatchwon, int plantbombpvpMatchlost, int plantbombpvpMatchplayed, int plantbombpvpBestscore, int generalpvpServershacked, int generalpvpServerdefender, int generalpvpServeraggression, int generalpvpHostagerescue, int generalpvpHostagedefense) {
//    public StatsEntity(@NonNull int id, String profileId, Date updateDate, int timePlayedRanked, int matchPlayedRanked, int matchWonRanked, int matchLostRanked, int killsRanked, int deathRanked, int timePlayedCasual, int matchPlayedCasual, int matchWonCasual, int matchLostCasual, int killsCasual, int deathCasual, int generalTimePlayed, int generalMatchPlayed, int generalMatchWon, int generalMatchLost, int generalKills, int generalDeath, int generalHeadshots, int generalBulletHit, int generalBulletFired, int generalKillAssists, int generalPenetrationKills, int generalMeleeKills, int generalRevive, int generalPvpBlindKills, int generalPvpRappelBreach) {
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
        this.generalPvpBlindKills = generalPvpBlindKills;
        this.generalPvpRappelBreach = generalPvpRappelBreach;
        this.generalPvpDbno = generalPvpDbno;
        this.generalPvpDbnoAssists = generalPvpDbnoAssists;
        this.generalPvpSuicide = generalPvpSuicide;
        this.generalPvpBarricadeDeployed = generalPvpBarricadeDeployed;
        this.generalPvpReinforcementDeploy = generalPvpReinforcementDeploy;
        this.generalPvpDistanceTravelled = generalPvpDistanceTravelled;
        this.generalPvpReviveDenied = generalPvpReviveDenied;
        this.generalPvpGadgetDestroy = generalPvpGadgetDestroy;
        this.generalPvpTotalXp = generalPvpTotalXp;
        this.secureareapvpTimeplayed = secureareapvpTimeplayed;
        this.rescuehostagepvpTimeplayed = rescuehostagepvpTimeplayed;
        this.plantbombpvpTimeplayed = plantbombpvpTimeplayed;
        this.secureareapvpMatchwon = secureareapvpMatchwon;
        this.secureareapvpMatchlost = secureareapvpMatchlost;
        this.secureareapvpMatchplayed = secureareapvpMatchplayed;
        this.secureareapvpBestscore = secureareapvpBestscore;
        this.rescuehostagepvpMatchwon = rescuehostagepvpMatchwon;
        this.rescuehostagepvpMatchlost = rescuehostagepvpMatchlost;
        this.rescuehostagepvpMatchplayed = rescuehostagepvpMatchplayed;
        this.rescuehostagepvpBestscore = rescuehostagepvpBestscore;
        this.plantbombpvpMatchwon = plantbombpvpMatchwon;
        this.plantbombpvpMatchlost = plantbombpvpMatchlost;
        this.plantbombpvpMatchplayed = plantbombpvpMatchplayed;
        this.plantbombpvpBestscore = plantbombpvpBestscore;
        this.generalpvpServershacked = generalpvpServershacked;
        this.generalpvpServerdefender = generalpvpServerdefender;
        this.generalpvpServeraggression = generalpvpServeraggression;
        this.generalpvpHostagerescue = generalpvpHostagerescue;
        this.generalpvpHostagedefense = generalpvpHostagedefense;
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

    public int getGeneralPvpBlindKills() {
        return generalPvpBlindKills;
    }

    public void setGeneralPvpBlindKills(int generalPvpBlindKills) {
        this.generalPvpBlindKills = generalPvpBlindKills;
    }

    public int getGeneralPvpRappelBreach() {
        return generalPvpRappelBreach;
    }

    public void setGeneralPvpRappelBreach(int generalPvpRappelBreach) {
        this.generalPvpRappelBreach = generalPvpRappelBreach;
    }

    public int getGeneralPvpDbno() {
        return generalPvpDbno;
    }

    public void setGeneralPvpDbno(int generalPvpDbno) {
        this.generalPvpDbno = generalPvpDbno;
    }

    public int getGeneralPvpDbnoAssists() {
        return generalPvpDbnoAssists;
    }

    public void setGeneralPvpDbnoAssists(int generalPvpDbnoAssists) {
        this.generalPvpDbnoAssists = generalPvpDbnoAssists;
    }

    public int getGeneralPvpSuicide() {
        return generalPvpSuicide;
    }

    public void setGeneralPvpSuicide(int generalPvpSuicide) {
        this.generalPvpSuicide = generalPvpSuicide;
    }

    public int getGeneralPvpBarricadeDeployed() {
        return generalPvpBarricadeDeployed;
    }

    public void setGeneralPvpBarricadeDeployed(int generalPvpBarricadeDeployed) {
        this.generalPvpBarricadeDeployed = generalPvpBarricadeDeployed;
    }

    public int getGeneralPvpReinforcementDeploy() {
        return generalPvpReinforcementDeploy;
    }

    public void setGeneralPvpReinforcementDeploy(int generalPvpReinforcementDeploy) {
        this.generalPvpReinforcementDeploy = generalPvpReinforcementDeploy;
    }

    public int getGeneralPvpDistanceTravelled() {
        return generalPvpDistanceTravelled;
    }

    public void setGeneralPvpDistanceTravelled(int generalPvpDistanceTravelled) {
        this.generalPvpDistanceTravelled = generalPvpDistanceTravelled;
    }

    public int getGeneralPvpReviveDenied() {
        return generalPvpReviveDenied;
    }

    public void setGeneralPvpReviveDenied(int generalPvpReviveDenied) {
        this.generalPvpReviveDenied = generalPvpReviveDenied;
    }

    public int getGeneralPvpGadgetDestroy() {
        return generalPvpGadgetDestroy;
    }

    public void setGeneralPvpGadgetDestroy(int generalPvpGadgetDestroy) {
        this.generalPvpGadgetDestroy = generalPvpGadgetDestroy;
    }

    public int getGeneralPvpTotalXp() {
        return generalPvpTotalXp;
    }

    public void setGeneralPvpTotalXp(int generalPvpTotalXp) {
        this.generalPvpTotalXp = generalPvpTotalXp;
    }

    public int getSecureareapvpTimeplayed() {
        return secureareapvpTimeplayed;
    }

    public void setSecureareapvpTimeplayed(int secureareapvpTimeplayed) {
        this.secureareapvpTimeplayed = secureareapvpTimeplayed;
    }

    public int getRescuehostagepvpTimeplayed() {
        return rescuehostagepvpTimeplayed;
    }

    public void setRescuehostagepvpTimeplayed(int rescuehostagepvpTimeplayed) {
        this.rescuehostagepvpTimeplayed = rescuehostagepvpTimeplayed;
    }

    public int getPlantbombpvpTimeplayed() {
        return plantbombpvpTimeplayed;
    }

    public void setPlantbombpvpTimeplayed(int plantbombpvpTimeplayed) {
        this.plantbombpvpTimeplayed = plantbombpvpTimeplayed;
    }

    public int getSecureareapvpMatchwon() {
        return secureareapvpMatchwon;
    }

    public void setSecureareapvpMatchwon(int secureareapvpMatchwon) {
        this.secureareapvpMatchwon = secureareapvpMatchwon;
    }

    public int getSecureareapvpMatchlost() {
        return secureareapvpMatchlost;
    }

    public void setSecureareapvpMatchlost(int secureareapvpMatchlost) {
        this.secureareapvpMatchlost = secureareapvpMatchlost;
    }

    public int getSecureareapvpMatchplayed() {
        return secureareapvpMatchplayed;
    }

    public void setSecureareapvpMatchplayed(int secureareapvpMatchplayed) {
        this.secureareapvpMatchplayed = secureareapvpMatchplayed;
    }

    public int getSecureareapvpBestscore() {
        return secureareapvpBestscore;
    }

    public void setSecureareapvpBestscore(int secureareapvpBestscore) {
        this.secureareapvpBestscore = secureareapvpBestscore;
    }

    public int getRescuehostagepvpMatchwon() {
        return rescuehostagepvpMatchwon;
    }

    public void setRescuehostagepvpMatchwon(int rescuehostagepvpMatchwon) {
        this.rescuehostagepvpMatchwon = rescuehostagepvpMatchwon;
    }

    public int getRescuehostagepvpMatchlost() {
        return rescuehostagepvpMatchlost;
    }

    public void setRescuehostagepvpMatchlost(int rescuehostagepvpMatchlost) {
        this.rescuehostagepvpMatchlost = rescuehostagepvpMatchlost;
    }

    public int getRescuehostagepvpMatchplayed() {
        return rescuehostagepvpMatchplayed;
    }

    public void setRescuehostagepvpMatchplayed(int rescuehostagepvpMatchplayed) {
        this.rescuehostagepvpMatchplayed = rescuehostagepvpMatchplayed;
    }

    public int getRescuehostagepvpBestscore() {
        return rescuehostagepvpBestscore;
    }

    public void setRescuehostagepvpBestscore(int rescuehostagepvpBestscore) {
        this.rescuehostagepvpBestscore = rescuehostagepvpBestscore;
    }

    public int getPlantbombpvpMatchwon() {
        return plantbombpvpMatchwon;
    }

    public void setPlantbombpvpMatchwon(int plantbombpvpMatchwon) {
        this.plantbombpvpMatchwon = plantbombpvpMatchwon;
    }

    public int getPlantbombpvpMatchlost() {
        return plantbombpvpMatchlost;
    }

    public void setPlantbombpvpMatchlost(int plantbombpvpMatchlost) {
        this.plantbombpvpMatchlost = plantbombpvpMatchlost;
    }

    public int getPlantbombpvpMatchplayed() {
        return plantbombpvpMatchplayed;
    }

    public void setPlantbombpvpMatchplayed(int plantbombpvpMatchplayed) {
        this.plantbombpvpMatchplayed = plantbombpvpMatchplayed;
    }

    public int getPlantbombpvpBestscore() {
        return plantbombpvpBestscore;
    }

    public void setPlantbombpvpBestscore(int plantbombpvpBestscore) {
        this.plantbombpvpBestscore = plantbombpvpBestscore;
    }

    public int getGeneralpvpServershacked() {
        return generalpvpServershacked;
    }

    public void setGeneralpvpServershacked(int generalpvpServershacked) {
        this.generalpvpServershacked = generalpvpServershacked;
    }

    public int getGeneralpvpServerdefender() {
        return generalpvpServerdefender;
    }

    public void setGeneralpvpServerdefender(int generalpvpServerdefender) {
        this.generalpvpServerdefender = generalpvpServerdefender;
    }

    public int getGeneralpvpServeraggression() {
        return generalpvpServeraggression;
    }

    public void setGeneralpvpServeraggression(int generalpvpServeraggression) {
        this.generalpvpServeraggression = generalpvpServeraggression;
    }

    public int getGeneralpvpHostagerescue() {
        return generalpvpHostagerescue;
    }

    public void setGeneralpvpHostagerescue(int generalpvpHostagerescue) {
        this.generalpvpHostagerescue = generalpvpHostagerescue;
    }

    public int getGeneralpvpHostagedefense() {
        return generalpvpHostagedefense;
    }

    public void setGeneralpvpHostagedefense(int generalpvpHostagedefense) {
        this.generalpvpHostagedefense = generalpvpHostagedefense;
    }
}
