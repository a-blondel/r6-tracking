package com.rainbow6.siege.r6_app.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class ConnectionEntity {

    @PrimaryKey
    @NonNull
    private String appId;
    private String encodedKey;
    private String ticket;
    private Date expiration;

    public ConnectionEntity(@NonNull String appId, String encodedKey, String ticket, Date expiration) {
        this.appId = appId;
        this.encodedKey = encodedKey;
        this.ticket = ticket;
        this.expiration = expiration;
    }

    @NonNull
    public String getAppId() {
        return appId;
    }

    public void setAppId(@NonNull String appId) {
        this.appId = appId;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
