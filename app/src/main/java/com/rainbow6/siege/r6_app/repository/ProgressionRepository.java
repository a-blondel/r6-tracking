package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.ProgressionDao;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;

import java.util.concurrent.ExecutionException;


public class ProgressionRepository {

    private ProgressionDao mProgressionDao;

    public ProgressionRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mProgressionDao = db.progressionDao();
    }


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

    public ProgressionEntity getLastProgressionEntityByProfileId(String profileId) {
        try {
            return new getLastProgressionEntityByProfileIdAsyncTask(mProgressionDao).execute(profileId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getLastProgressionEntityByProfileIdAsyncTask extends AsyncTask<String, Void, ProgressionEntity> {

        private ProgressionDao mAsyncTaskDao;

        getLastProgressionEntityByProfileIdAsyncTask(ProgressionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected ProgressionEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getLastProgressionEntityByProfileId(params[0]);
        }
    }
}
