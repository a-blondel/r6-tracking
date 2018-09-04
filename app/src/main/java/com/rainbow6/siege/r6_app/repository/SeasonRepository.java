package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.SeasonDao;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;


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

    public void getLastSeasonEntity(String profileId) {
        new getLastSeasonEntityAsyncTask(mSeasonDao).execute(profileId);
    }

    private static class getLastSeasonEntityAsyncTask extends AsyncTask<String, Void, Void> {

        private SeasonDao mAsyncTaskDao;

        getLastSeasonEntityAsyncTask(SeasonDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getLastSeasonEntity(params[0]);
            return null;
        }
    }
}
