package com.rainbow6.siege.r6_app.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.db.AppDatabase;
import com.rainbow6.siege.r6_app.db.dao.PlayerDao;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;


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

    public PlayerEntity getPlayerByProfileId (String profileId) {
        try{
            return new getPlayerByNameAsyncTask(mPlayerDao).execute(profileId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getPlayerByNameAsyncTask extends AsyncTask<String, Void, PlayerEntity> {

        private PlayerDao mAsyncTaskDao;

        getPlayerByNameAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected PlayerEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getPlayerByProfileId(params[0]);
        }
    }
}
