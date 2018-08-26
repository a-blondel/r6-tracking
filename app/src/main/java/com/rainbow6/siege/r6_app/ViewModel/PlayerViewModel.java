package com.rainbow6.siege.r6_app.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.rainbow6.siege.r6_app.Entities.Player;
import com.rainbow6.siege.r6_app.Repository.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository mPlayerRepository;

    private LiveData<List<Player>> mAllPlayers;

    public PlayerViewModel(Application application) {
        super(application);
        mPlayerRepository = new PlayerRepository(application);
        mAllPlayers = mPlayerRepository.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() { return mAllPlayers; }

    public void insert(Player player) { mPlayerRepository.insert(player); }

    public void delete(Player player) { mPlayerRepository.delete(player); }

//    public Player(String name) { mPlayerRepository.getPlayerByName(name); }

}
