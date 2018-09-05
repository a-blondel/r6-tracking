package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;

@Dao
public interface ProgressionDao {

    @Insert
    void insert(ProgressionEntity progressionEntity);

    @Query("SELECT * FROM ProgressionEntity WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    ProgressionEntity getLastProgressionEntityByProfileId(String profileId);

}
