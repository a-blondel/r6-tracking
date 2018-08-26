package com.rainbow6.siege.r6_app.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.Entities.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insert(Player player);

    @Query("SELECT * FROM player WHERE nameOnPlatform = :name")
    Player getPlayerByName(String name);

    @Query("SELECT * from player ORDER BY nameOnPlatform ASC")
    LiveData<List<Player>> getAllPlayers();

    @Delete
    void delete(Player player);


}
