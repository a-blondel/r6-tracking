package com.rainbow6.siege.r6_app.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rainbow6.siege.r6_app.Entities.Connect;

@Dao
public interface ConnectDao {

    @Insert
    void insert(Connect connect);

    @Query("SELECT * FROM connect WHERE appId = :appId")
    Connect getToken(String appId);

    @Update
    void update(Connect connect);

}
