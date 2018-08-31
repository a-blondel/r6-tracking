package com.rainbow6.siege.r6_app.repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.dao.ProgressionDao;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;


public class ProgressionRepository {

    private ProgressionDao mProgressionDao;


    public void insert (ProgressionEntity progressionEntity) {
        new insertAsyncTask(mProgressionDao).execute(progressionEntity);
    }

    private static class insertAsyncTask extends AsyncTask<ProgressionEntity, Void, Void> {

        private ProgressionDao mAsyncTaskDao;

        insertAsyncTask(ProgressionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ProgressionEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getLastProgression (String profileId) {
        new getLastProgressionAsyncTask(mProgressionDao).execute(profileId);
    }

    private static class getLastProgressionAsyncTask extends AsyncTask<String, Void, Void> {

        private ProgressionDao mAsyncTaskDao;

        getLastProgressionAsyncTask(ProgressionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getLastProgression(params[0]);
            return null;
        }
    }
}
