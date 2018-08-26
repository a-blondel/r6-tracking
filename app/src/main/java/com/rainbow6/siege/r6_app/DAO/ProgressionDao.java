package com.rainbow6.siege.r6_app.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.Entities.Progression;

@Dao
public interface ProgressionDao {

    @Insert
    void insert(Progression progression);

    @Query("SELECT * FROM progression WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    Progression getLastProgression(String profileId);

}
