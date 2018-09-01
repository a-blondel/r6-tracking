package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.ConnectionDao;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;

import java.util.concurrent.ExecutionException;

public class ConnectionRepository {

    private ConnectionDao mConnectionDao;

    public ConnectionRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mConnectionDao = db.connectionDao();
    }

    public void insert (ConnectionEntity connectionEntity) {
        new insertAsyncTask(mConnectionDao).execute(connectionEntity);
    }

    private static class insertAsyncTask extends AsyncTask<ConnectionEntity, Void, Void> {

        private ConnectionDao mAsyncTaskDao;

        insertAsyncTask(ConnectionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ConnectionEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    public ConnectionEntity getConnection(String appId) {
        try {
            return new getConnectionAsyncTask(mConnectionDao).execute(appId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getConnectionAsyncTask extends AsyncTask<String, Void, ConnectionEntity> {

        private ConnectionDao mAsyncTaskDao;

        getConnectionAsyncTask(ConnectionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected ConnectionEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getConnection(params[0]);
        }
    }

}
