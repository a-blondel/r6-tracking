package com.rainbow6.siege.r6_app.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.rainbow6.siege.r6_app.DAO.PlayerDao;
import com.rainbow6.siege.r6_app.Database.RainbowRoomDatabase;
import com.rainbow6.siege.r6_app.Entities.Player;

import java.util.List;


public class PlayerRepository {

    private PlayerDao mPlayerDao;
    private LiveData<List<Player>> mAllPlayers;

    public PlayerRepository(Application application){
        RainbowRoomDatabase db = RainbowRoomDatabase.getDatabase(application);
        mPlayerDao = db.playerDao();
        mAllPlayers = mPlayerDao.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }


    public void insert (Player player) {
        new insertAsyncTask(mPlayerDao).execute(player);
    }

    private static class insertAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        insertAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Player... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete (Player player) {
        new deleteAsyncTask(mPlayerDao).execute(player);
    }

    private static class deleteAsyncTask extends AsyncTask<Player, Void, Void> {

        private PlayerDao mAsyncTaskDao;

        deleteAsyncTask(PlayerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Player... params) {
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
