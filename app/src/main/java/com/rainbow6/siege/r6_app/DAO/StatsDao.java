package com.rainbow6.siege.r6_app.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.Entities.Stats;

@Dao
public interface StatsDao {

    @Insert
    void insert(Stats stats);

    @Query("SELECT * FROM stats WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    Stats getLastStats(String profileId);

}
