package com.rainbow6.siege.r6_app.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.ConnectDao;
import com.rainbow6.siege.r6_app.Database.RainbowRoomDatabase;
import com.rainbow6.siege.r6_app.Entities.Connect;

public class ConnectRepository {

    private ConnectDao mConnectDao;

    public ConnectRepository(Application application){
        RainbowRoomDatabase db = RainbowRoomDatabase.getDatabase(application);
        mConnectDao = db.connectDao();
    }

    public void insert (Connect connect) {
        new insertAsyncTask(mConnectDao).execute(connect);
    }

    private static class insertAsyncTask extends AsyncTask<Connect, Void, Void> {

        private ConnectDao mAsyncTaskDao;

        insertAsyncTask(ConnectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Connect... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update (Connect connect) {
        new updateAsyncTask(mConnectDao).execute(connect);
    }

    private static class updateAsyncTask extends AsyncTask<Connect, Void, Void> {

        private ConnectDao mAsyncTaskDao;

        updateAsyncTask(ConnectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Connect... params) {
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
