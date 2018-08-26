package com.rainbow6.siege.r6_app.Repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.SynchDao;
import com.rainbow6.siege.r6_app.Entities.Synch;


public class SynchRepository {

    private SynchDao mSynchDao;


    public void insert (Synch synch) {
        new insertAsyncTask(mSynchDao).execute(synch);
    }

    private static class insertAsyncTask extends AsyncTask<Synch, Void, Void> {

        private SynchDao mAsyncTaskDao;

        insertAsyncTask(SynchDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Synch... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getSynchParams (String profileId) {
        new getSynchParamsAsyncTask(mSynchDao).execute(profileId);
    }

    private static class getSynchParamsAsyncTask extends AsyncTask<String, Void, Void> {

        private SynchDao mAsyncTaskDao;

        getSynchParamsAsyncTask(SynchDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getSynchParams(params[0]);
            return null;
        }
    }
}
