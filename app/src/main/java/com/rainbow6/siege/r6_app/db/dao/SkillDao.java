package com.rainbow6.siege.r6_app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.db.entity.SkillEntity;

@Dao
public interface SkillDao {

    @Insert
    void insert(SkillEntity skillEntity);

    @Query("SELECT * FROM SkillEntity WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    SkillEntity getLastSkill(String profileId);

}
