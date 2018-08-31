package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.SynchEntity;

@Dao
public interface SynchDao {

    @Insert
    void insert(SynchEntity synchEntity);

    @Query("SELECT * FROM SynchEntity WHERE profileId = :profileId")
    SynchEntity getSynchParams(String profileId);

}
