package com.rainbow6.siege.r6_app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;
import com.rainbow6.siege.r6_app.repository.PlayerRepository;
import com.rainbow6.siege.r6_app.repository.ProgressionRepository;
import com.rainbow6.siege.r6_app.repository.SeasonRepository;
import com.rainbow6.siege.r6_app.repository.StatsRepository;
import com.rainbow6.siege.r6_app.repository.SyncRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository playerRepository;
    private ProgressionRepository progressionRepository;
    private SeasonRepository seasonRepository;
    private StatsRepository statsRepository;
    private SyncRepository syncRepository;

    private LiveData<List<PlayerEntity>> mAllPlayers;

    public PlayerViewModel(Application application) {
        super(application);
        playerRepository = new PlayerRepository(application);
        mAllPlayers = playerRepository.getAllPlayers();
        progressionRepository = new ProgressionRepository(application);
        seasonRepository = new SeasonRepository(application);
        statsRepository = new StatsRepository(application);
        syncRepository = new SyncRepository(application);
    }

    public LiveData<List<PlayerEntity>> getAllPlayers() { return mAllPlayers; }

    public void insertPlayer(PlayerEntity playerEntity) { playerRepository.insert(playerEntity); }

    public void insertProgression(ProgressionEntity progressionEntity) { progressionRepository.insert(progressionEntity); }

    public void insertSeason(SeasonEntity seasonEntity) { seasonRepository.insert(seasonEntity); }

    public void insertStats(StatsEntity statsEntity) { statsRepository.insert(statsEntity); }

    public void insertSync(SyncEntity syncEntity) { syncRepository.insert(syncEntity); }

    public void delete(PlayerEntity playerEntity) { playerRepository.delete(playerEntity); }

//    public PlayerEntity(String name) { playerRepository.getPlayerByName(name); }

}
