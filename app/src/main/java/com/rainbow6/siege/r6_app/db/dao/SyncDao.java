package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rainbow6.siege.r6_app.db.entity.SyncEntity;

@Dao
public interface SyncDao {

    @Insert
    void insert(SyncEntity syncEntity);

    @Update
    void update(SyncEntity syncEntity);

    @Query("SELECT * FROM SyncEntity WHERE profileId = :profileId")
    SyncEntity getSyncByProfileId(String profileId);

}
