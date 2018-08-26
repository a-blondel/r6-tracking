package com.rainbow6.siege.r6_app.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rainbow6.siege.r6_app.Entities.Skill;

@Dao
public interface SkillDao {

    @Insert
    void insert(Skill skill);

    @Query("SELECT * FROM skill WHERE profileId = :profileId ORDER BY updateDate DESC LIMIT 1")
    Skill getLastSkill(String profileId);

}
