package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.SyncDao;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;


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

    public void getSyncParams(String profileId) {
        new getSyncParamsAsyncTask(mSyncDao).execute(profileId);
    }

    private static class getSyncParamsAsyncTask extends AsyncTask<String, Void, Void> {

        private SyncDao mAsyncTaskDao;

        getSyncParamsAsyncTask(SyncDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getSyncParams(params[0]);
            return null;
        }
    }
}
