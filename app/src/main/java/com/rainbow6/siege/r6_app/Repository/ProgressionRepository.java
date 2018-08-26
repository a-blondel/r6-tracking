package com.rainbow6.siege.r6_app.Repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.ProgressionDao;
import com.rainbow6.siege.r6_app.Entities.Progression;


public class ProgressionRepository {

    private ProgressionDao mProgressionDao;


    public void insert (Progression progression) {
        new insertAsyncTask(mProgressionDao).execute(progression);
    }

    private static class insertAsyncTask extends AsyncTask<Progression, Void, Void> {

        private ProgressionDao mAsyncTaskDao;

        insertAsyncTask(ProgressionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Progression... params) {
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
