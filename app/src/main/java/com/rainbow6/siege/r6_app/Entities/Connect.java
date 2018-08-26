package com.rainbow6.siege.r6_app.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Connect {

    @PrimaryKey
    @NonNull
    private String appId;
    private String ticket;

    public Connect(String appId, String ticket) {
        this.appId = appId;
        this.ticket = ticket;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
