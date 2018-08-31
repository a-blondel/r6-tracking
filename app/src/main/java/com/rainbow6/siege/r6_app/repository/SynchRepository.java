package com.rainbow6.siege.r6_app.repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.dao.SynchDao;
import com.rainbow6.siege.r6_app.db.entity.SynchEntity;


public class SynchRepository {

    private SynchDao mSynchDao;


    public void insert (SynchEntity synchEntity) {
        new insertAsyncTask(mSynchDao).execute(synchEntity);
    }

    private static class insertAsyncTask extends AsyncTask<SynchEntity, Void, Void> {

        private SynchDao mAsyncTaskDao;

        insertAsyncTask(SynchDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SynchEntity... params) {
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
