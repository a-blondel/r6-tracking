package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.GeneralEntity;

@Dao
public interface GeneralDao {

    @Insert
    void insert(GeneralEntity generalEntity);

    @Query("SELECT * FROM GeneralEntity WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    GeneralEntity getLastGeneral(String profileId);

}
