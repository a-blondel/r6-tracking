package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.SyncDao;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;

import java.util.concurrent.ExecutionException;


public class SyncRepository {

    private SyncDao mSyncDao;

    public SyncRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mSyncDao = db.syncDao();
    }

    public void insert (SyncEntity syncEntity) {
        new insertAsyncTask(mSyncDao).execute(syncEntity);
    }

    private static class insertAsyncTask extends AsyncTask<SyncEntity, Void, Void> {

        private SyncDao mAsyncTaskDao;

        insertAsyncTask(SyncDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SyncEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update (SyncEntity syncEntity) {
        new updateAsyncTask(mSyncDao).execute(syncEntity);
    }

    private static class updateAsyncTask extends AsyncTask<SyncEntity, Void, Void> {

        private SyncDao mAsyncTaskDao;

        updateAsyncTask(SyncDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SyncEntity... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public SyncEntity getSyncByProfileId(String profileId) {
        try {
            return new getSyncByProfileIdAsyncTask(mSyncDao).execute(profileId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getSyncByProfileIdAsyncTask extends AsyncTask<String, Void, SyncEntity> {

        private SyncDao mAsyncTaskDao;

        getSyncByProfileIdAsyncTask(SyncDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected SyncEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getSyncByProfileId(params[0]);
        }
    }
}
