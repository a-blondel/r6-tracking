package com.rainbow6.siege.r6_app.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insert(PlayerEntity playerEntity);

    @Query("SELECT * FROM PlayerEntity WHERE profileId = :profileId")
    PlayerEntity getPlayerByProfileId(String profileId);

    @Query("SELECT * from PlayerEntity ORDER BY nameOnPlatform ASC")
    LiveData<List<PlayerEntity>> getAllPlayers();

    @Delete
    void delete(PlayerEntity playerEntity);

}
