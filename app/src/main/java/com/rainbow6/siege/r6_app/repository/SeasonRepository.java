package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.SeasonDao;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class SeasonRepository {

    private SeasonDao mSeasonDao;

    public SeasonRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mSeasonDao = db.seasonDao();
    }


    public void insert (SeasonEntity seasonEntity) {
        new insertAsyncTask(mSeasonDao).execute(seasonEntity);
    }

    private static class insertAsyncTask extends AsyncTask<SeasonEntity, Void, Void> {

        private SeasonDao mAsyncTaskDao;

        insertAsyncTask(SeasonDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SeasonEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public SeasonEntity getLastSeasonEntityByProfileIdAndRegionId(String profileId, String regionId, String skip, String count) {
        try {
            return new getLastSeasonEntityByProfileIdAndRegionIdAsyncTask(mSeasonDao).execute(profileId, regionId, skip, count).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getLastSeasonEntityByProfileIdAndRegionIdAsyncTask extends AsyncTask<String, Void, SeasonEntity> {
        private SeasonDao mAsyncTaskDao;
        getLastSeasonEntityByProfileIdAndRegionIdAsyncTask(SeasonDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected SeasonEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getLastSeasonEntityByProfileIdAndRegionId(params[0], params[1], params[2], params[3]);
        }
    }


    public List<SeasonEntity> getSeasonEntityHistoryByProfileId(String profileId, String skip, String count) {
        try {
            return new getSeasonEntityHistoryByProfileIdAsyncTask(mSeasonDao).execute(profileId, skip, count).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getSeasonEntityHistoryByProfileIdAsyncTask extends AsyncTask<String, Void, List<SeasonEntity>> {
        private SeasonDao mAsyncTaskDao;
        getSeasonEntityHistoryByProfileIdAsyncTask(SeasonDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected List<SeasonEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getSeasonEntityHistoryByProfileIdAsyncTask(params[0], params[1], params[2]);
        }
    }

    public SeasonEntity getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate(String profileId, String regionId, int season, Date updateDate) {
        try {
            return new getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate(mSeasonDao).execute(new MyTaskParams(profileId, regionId, season, updateDate)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate extends AsyncTask<MyTaskParams, Void, SeasonEntity> {
        private SeasonDao mAsyncTaskDao;

        getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate(SeasonDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected SeasonEntity doInBackground(final MyTaskParams... params) {
            return mAsyncTaskDao.getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate(params[0].profileId, params[0].regionId, params[0].season, params[0].updateDate);
        }
    }

    private static class MyTaskParams {
        String profileId;
        String regionId;
        int season;
        Date updateDate;

        MyTaskParams(String profileId, String regionId, int season, Date updateDate) {
            this.profileId = profileId;
            this.regionId = regionId;
            this.season = season;
            this.updateDate = updateDate;
        }
    }

}
