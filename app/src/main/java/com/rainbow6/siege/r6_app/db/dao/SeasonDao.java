package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;

@Dao
public interface SeasonDao {

    @Insert
    void insert(SeasonEntity seasonEntity);

    @Query("SELECT * FROM SeasonEntity WHERE profileId = :profileId AND region = :regionId ORDER BY updateDate DESC LIMIT :skip, :count")
    SeasonEntity getLastSeasonEntityByProfileIdAndRegionId(String profileId, String regionId, String skip, String count);

}
