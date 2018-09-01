package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;

@Dao
public interface ConnectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConnectionEntity connectionEntity);

    @Query("SELECT * FROM ConnectionEntity WHERE appId = :appId")
    ConnectionEntity getConnection(String appId);

}
