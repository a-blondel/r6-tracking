package com.rainbow6.siege.r6_app.Repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.SkillDao;
import com.rainbow6.siege.r6_app.Entities.Skill;


public class SkillRepository {

    private SkillDao mSkillDao;


    public void insert (Skill skill) {
        new insertAsyncTask(mSkillDao).execute(skill);
    }

    private static class insertAsyncTask extends AsyncTask<Skill, Void, Void> {

        private SkillDao mAsyncTaskDao;

        insertAsyncTask(SkillDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Skill... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getLastSkill (String profileId) {
        new getLastSkillAsyncTask(mSkillDao).execute(profileId);
    }

    private static class getLastSkillAsyncTask extends AsyncTask<String, Void, Void> {

        private SkillDao mAsyncTaskDao;

        getLastSkillAsyncTask(SkillDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getLastSkill(params[0]);
            return null;
        }
    }
}
