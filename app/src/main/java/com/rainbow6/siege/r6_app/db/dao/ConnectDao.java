package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rainbow6.siege.r6_app.db.entity.ConnectEntity;

@Dao
public interface ConnectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConnectEntity connectEntity);

    @Query("SELECT * FROM ConnectEntity WHERE appId = :appId")
    ConnectEntity getConnect(String appId);

    @Update
    void update(ConnectEntity connectEntity);

}
