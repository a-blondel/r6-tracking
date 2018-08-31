package com.rainbow6.siege.r6_app.repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.dao.GeneralDao;
import com.rainbow6.siege.r6_app.db.entity.GeneralEntity;


public class GeneralRepository {

    private GeneralDao mGeneralDao;


    public void insert (GeneralEntity generalEntity) {
        new insertAsyncTask(mGeneralDao).execute(generalEntity);
    }

    private static class insertAsyncTask extends AsyncTask<GeneralEntity, Void, Void> {

        private GeneralDao mAsyncTaskDao;

        insertAsyncTask(GeneralDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GeneralEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getLastGeneral (String profileId) {
        new getLastGeneralAsyncTask(mGeneralDao).execute(profileId);
    }

    private static class getLastGeneralAsyncTask extends AsyncTask<String, Void, Void> {

        private GeneralDao mAsyncTaskDao;

        getLastGeneralAsyncTask(GeneralDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getLastGeneral(params[0]);
            return null;
        }
    }
}
