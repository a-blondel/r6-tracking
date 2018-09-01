package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.PlayerDao;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;

import java.util.List;


public class PlayerRepository {

    private PlayerDao mPlayerDao;
    private LiveData<List<PlayerEntity>> mAllPlayers;

    public PlayerRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mPlayerDao = db.playerDao();
        mAllPlayers = mPlayerDao.getAllPlayers();
    }

    public LiveData<List<PlayerEntity>> getAllPlayers() {
        return mAllPlayers;
    }


    public void insert (PlayerEntity playerEntity) {
        new insertAsyncTask(mPlayerDao).execute(playerEntity);
    }

    private static class insertAsyncTask extends AsyncTask<PlayerEntity, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        insertAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlayerEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete (PlayerEntity playerEntity) {
        new deleteAsyncTask(mPlayerDao).execute(playerEntity);
    }

    private static class deleteAsyncTask extends AsyncTask<PlayerEntity, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        deleteAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PlayerEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void getPlayerByName (String name) {
        new getPlayerByNameAsyncTask(mPlayerDao).execute(name);
    }

    private static class getPlayerByNameAsyncTask extends AsyncTask<String, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        getPlayerByNameAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.getPlayerByName(params[0]);
            return null;
        }
    }
}
