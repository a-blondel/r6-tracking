package com.rainbow6.siege.r6_app.repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.dao.SkillDao;
import com.rainbow6.siege.r6_app.db.entity.SkillEntity;


public class SkillRepository {

    private SkillDao mSkillDao;


    public void insert (SkillEntity skillEntity) {
        new insertAsyncTask(mSkillDao).execute(skillEntity);
    }

    private static class insertAsyncTask extends AsyncTask<SkillEntity, Void, Void> {

        private SkillDao mAsyncTaskDao;

        insertAsyncTask(SkillDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SkillEntity... params) {
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
