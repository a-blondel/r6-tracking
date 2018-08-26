package com.rainbow6.siege.r6_app.Repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.StatsDao;
import com.rainbow6.siege.r6_app.Entities.Stats;


public class StatsRepository {

    private StatsDao mStatsDao;


    public void insert (Stats stats) {
        new insertAsyncTask(mStatsDao).execute(stats);
    }

    private static class insertAsyncTask extends AsyncTask<Stats, Void, Void> {

        private StatsDao mAsyncTaskDao;

        insertAsyncTask(StatsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stats... params) {
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
