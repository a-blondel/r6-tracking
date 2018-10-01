package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.StatsEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface StatsDao {

    @Insert
    void insert(StatsEntity statsEntity);

    @Query("SELECT * FROM StatsEntity WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    StatsEntity getLastStatsEntityByProfileId(String profileId);

    @Query("SELECT * FROM StatsEntity WHERE profileId = :profileId AND updateDate > :updateDate ORDER BY updateDate ASC LIMIT 1")
    StatsEntity getStatsEntityByProfileIdAndGreaterThanDate(String profileId, Date updateDate);

    @Query("SELECT * FROM StatsEntity WHERE profileId = :profileId AND updateDate < :updateDate ORDER BY updateDate DESC LIMIT 1")
    StatsEntity getStatsEntityByProfileIdAndLessThanDate(String profileId, Date updateDate);

}
