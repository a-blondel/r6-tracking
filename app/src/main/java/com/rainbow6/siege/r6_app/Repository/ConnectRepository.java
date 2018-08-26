package com.rainbow6.siege.r6_app.Repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.ConnectDao;
import com.rainbow6.siege.r6_app.Entities.Connect;

public class ConnectRepository {

    private ConnectDao mConnectDao;


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

    public void getToken (String appId) {
        new getTokenAsyncTask(mConnectDao).execute(appId);
    }

    private static class getTokenAsyncTask extends AsyncTask<String, Void, Void> {

        private ConnectDao mAsyncTaskDao;

        getTokenAsyncTask(ConnectDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getToken(params[0]);
            return null;
        }
    }
}
