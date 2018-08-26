package com.rainbow6.siege.r6_app.Repository;

import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.GeneralDao;
import com.rainbow6.siege.r6_app.Entities.General;


public class GeneralRepository {

    private GeneralDao mGeneralDao;


    public void insert (General general) {
        new insertAsyncTask(mGeneralDao).execute(general);
    }

    private static class insertAsyncTask extends AsyncTask<General, Void, Void> {

        private GeneralDao mAsyncTaskDao;

        insertAsyncTask(GeneralDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final General... params) {
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
