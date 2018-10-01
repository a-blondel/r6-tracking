package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.StatsDao;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class StatsRepository {

    private StatsDao mStatsDao;

    public StatsRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mStatsDao = db.statsDao();
    }


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

    public StatsEntity getLastStatsEntityByProfileId(String profileId) {
        try {
            return new getLastStatsEntityByProfileIdAsyncTask(mStatsDao).execute(profileId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getLastStatsEntityByProfileIdAsyncTask extends AsyncTask<String, Void, StatsEntity> {

        private StatsDao mAsyncTaskDao;

        getLastStatsEntityByProfileIdAsyncTask(StatsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected StatsEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getLastStatsEntityByProfileId(params[0]);
        }
    }

    public StatsEntity getStatsEntityByProfileIdAndGreaterThanDate(String profileId, Date updateDate) {
        try {
            return new getStatsEntityByProfileIdAndGreaterThanDateAsyncTask(mStatsDao).execute(new MyTaskParams(profileId, updateDate)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getStatsEntityByProfileIdAndGreaterThanDateAsyncTask extends AsyncTask<MyTaskParams, Void, StatsEntity> {

        private StatsDao mAsyncTaskDao;

        getStatsEntityByProfileIdAndGreaterThanDateAsyncTask(StatsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected StatsEntity doInBackground(final MyTaskParams... params) {
            return mAsyncTaskDao.getStatsEntityByProfileIdAndGreaterThanDate(params[0].profileId, params[0].updateDate);
        }
    }

    public StatsEntity getStatsEntityByProfileIdAndLessThanDate(String profileId, Date updateDate) {
        try {
            return new getStatsEntityByProfileIdAndLessThanDateAsyncTask(mStatsDao).execute(new MyTaskParams(profileId, updateDate)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getStatsEntityByProfileIdAndLessThanDateAsyncTask extends AsyncTask<MyTaskParams, Void, StatsEntity> {

        private StatsDao mAsyncTaskDao;

        getStatsEntityByProfileIdAndLessThanDateAsyncTask(StatsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected StatsEntity doInBackground(final MyTaskParams... params) {
            return mAsyncTaskDao.getStatsEntityByProfileIdAndLessThanDate(params[0].profileId, params[0].updateDate);
        }
    }

    private static class MyTaskParams {
        String profileId;
        Date updateDate;

        MyTaskParams(String profileId, Date updateDate) {
            this.profileId = profileId;
            this.updateDate = updateDate;
        }
    }

}
