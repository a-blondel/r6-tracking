package com.rainbow6.siege.r6_app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.repository.ConnectionRepository;
import com.rainbow6.siege.r6_app.repository.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository mPlayerRepository;
    private ConnectionRepository mConnectRepository;

    private LiveData<List<PlayerEntity>> mAllPlayers;

    public PlayerViewModel(Application application) {
        super(application);
        mPlayerRepository = new PlayerRepository(application);
        mAllPlayers = mPlayerRepository.getAllPlayers();
        mConnectRepository = new ConnectionRepository(application);
    }

    public LiveData<List<PlayerEntity>> getAllPlayers() { return mAllPlayers; }

    public void insert(PlayerEntity playerEntity) { mPlayerRepository.insert(playerEntity); }

    public void delete(PlayerEntity playerEntity) { mPlayerRepository.delete(playerEntity); }

//    public PlayerEntity(String name) { mPlayerRepository.getPlayerByName(name); }

    public void insert(ConnectionEntity connectionEntity) { mConnectRepository.insert(connectionEntity); }

    public ConnectionEntity getConnection(String appId) { return mConnectRepository.getConnection(appId); }

}
