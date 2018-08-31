package com.rainbow6.siege.r6_app.repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.dao.StatsDao;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;


public class StatsRepository {

    private StatsDao mStatsDao;


    public void insert (StatsEntity statsEntity) {
        new insertAsyncTask(mStatsDao).execute(statsEntity);
    }

    private static class insertAsyncTask extends AsyncTask<StatsEntity, Void, Void> {

        private StatsDao mAsyncTaskDao;

        insertAsyncTask(StatsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final StatsEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getLastStats (String profileId) {
        new getLastStatsAsyncTask(mStatsDao).execute(profileId);
    }

    private static class getLastStatsAsyncTask extends AsyncTask<String, Void, Void> {

        private StatsDao mAsyncTaskDao;

        getLastStatsAsyncTask(StatsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getLastStats(params[0]);
            return null;
        }
    }
}
