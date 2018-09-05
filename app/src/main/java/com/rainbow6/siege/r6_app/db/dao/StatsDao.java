package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.StatsEntity;

@Dao
public interface StatsDao {

    @Insert
    void insert(StatsEntity statsEntity);

    @Query("SELECT * FROM StatsEntity WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    StatsEntity getLastStatsEntityByProfileId(String profileId);

}
