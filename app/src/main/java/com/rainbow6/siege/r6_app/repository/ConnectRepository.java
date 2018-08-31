package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppRoomDatabase;
import com.rainbow6.siege.r6_app.db.dao.ConnectDao;
import com.rainbow6.siege.r6_app.db.entity.ConnectEntity;

public class ConnectRepository {

    private ConnectDao mConnectDao;

    public ConnectRepository(Application application){
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mConnectDao = db.connectDao();
    }

    public void insert (ConnectEntity connectEntity) {
        new insertAsyncTask(mConnectDao).execute(connectEntity);
    }

    private static class insertAsyncTask extends AsyncTask<ConnectEntity, Void, Void> {

        private ConnectDao mAsyncTaskDao;

        insertAsyncTask(ConnectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ConnectEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update (ConnectEntity connectEntity) {
        new updateAsyncTask(mConnectDao).execute(connectEntity);
    }

    private static class updateAsyncTask extends AsyncTask<ConnectEntity, Void, Void> {

        private ConnectDao mAsyncTaskDao;

        updateAsyncTask(ConnectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ConnectEntity... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public void getConnect (String appId) {
        new getConnectAsyncTask(mConnectDao).execute(appId);
    }

    private static class getConnectAsyncTask extends AsyncTask<String, Void, Void> {

        private ConnectDao mAsyncTaskDao;

        getConnectAsyncTask(ConnectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getConnect(params[0]);
            return null;
        }
    }
}
