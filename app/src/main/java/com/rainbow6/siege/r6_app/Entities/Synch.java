package com.rainbow6.siege.r6_app.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
public class Synch {

    @PrimaryKey
    @NonNull
    private String profileId;
    private boolean synchEmea;
    private boolean synchNcsa;
    private boolean synchApac;
    private int synchDelay;
    private boolean synchGeneral;
    private boolean synchRanked;
    private boolean synchCasual;

    public Synch(String profileId, boolean synchEmea, boolean synchNcsa, boolean synchApac, int synchDelay, boolean synchGeneral, boolean synchRanked, boolean synchCasual) {
        this.profileId = profileId;
        this.synchEmea = synchEmea;
        this.synchNcsa = synchNcsa;
        this.synchApac = synchApac;
        this.synchDelay = synchDelay;
        this.synchGeneral = synchGeneral;
        this.synchRanked = synchRanked;
        this.synchCasual = synchCasual;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public boolean isSynchEmea() {
        return synchEmea;
    }

    public void setSynchEmea(boolean synchEmea) {
        this.synchEmea = synchEmea;
    }

    public boolean isSynchNcsa() {
        return synchNcsa;
    }

    public void setSynchNcsa(boolean synchNcsa) {
        this.synchNcsa = synchNcsa;
    }

    public boolean isSynchApac() {
        return synchApac;
    }

    public void setSynchApac(boolean synchApac) {
        this.synchApac = synchApac;
    }

    public int getSynchDelay() {
        return synchDelay;
    }

    public void setSynchDelay(int synchDelay) {
        this.synchDelay = synchDelay;
    }

    public boolean isSynchGeneral() {
        return synchGeneral;
    }

    public void setSynchGeneral(boolean synchGeneral) {
        this.synchGeneral = synchGeneral;
    }

    public boolean isSynchRanked() {
        return synchRanked;
    }

    public void setSynchRanked(boolean synchRanked) {
        this.synchRanked = synchRanked;
    }

    public boolean isSynchCasual() {
        return synchCasual;
    }

    public void setSynchCasual(boolean synchCasual) {
        this.synchCasual = synchCasual;
    }
}
