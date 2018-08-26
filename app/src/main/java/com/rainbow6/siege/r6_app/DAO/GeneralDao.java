package com.rainbow6.siege.r6_app.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.Entities.General;

@Dao
public interface GeneralDao {

    @Insert
    void insert(General general);

    @Query("SELECT * FROM general WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    General getLastGeneral(String profileId);

}
