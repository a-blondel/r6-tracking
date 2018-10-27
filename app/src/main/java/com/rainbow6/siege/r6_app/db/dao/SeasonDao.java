package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface SeasonDao {

    @Insert
    void insert(SeasonEntity seasonEntity);

    @Query("SELECT * FROM SeasonEntity WHERE profileId = :profileId AND region = :regionId ORDER BY season DESC, updateDate DESC LIMIT :skip, :count")
    SeasonEntity getLastSeasonEntityByProfileIdAndRegionId(String profileId, String regionId, String skip, String count);

    @Query("SELECT * FROM SeasonEntity WHERE profileId = :profileId ORDER BY season DESC, updateDate DESC LIMIT :skip, :count")
    List<SeasonEntity> getSeasonEntityHistoryByProfileIdAsyncTask(String profileId, String skip, String count);

    @Query("SELECT * FROM SeasonEntity WHERE profileId = :profileId AND region = :regionId AND season = :season AND updateDate < :updateDate ORDER BY updateDate DESC LIMIT 1")
    SeasonEntity getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate(String profileId, String regionId, int season, Date updateDate);

}
