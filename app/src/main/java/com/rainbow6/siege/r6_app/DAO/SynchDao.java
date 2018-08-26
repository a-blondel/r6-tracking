package com.rainbow6.siege.r6_app.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.Entities.Synch;

@Dao
public interface SynchDao {

    @Insert
    void insert(Synch synch);

    @Query("SELECT * FROM synch WHERE profileId = :profileId")
    Synch getSynchParams(String profileId);

}
