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

import java.util.Date;
import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    public static String SKIP_0 = "0";
    public static String SKIP_1 = "1";
    public static String COUNT_1 = "1";
    public static String COUNT_10 = "10";

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

    public PlayerEntity getPlayerByProfileId(String profileId) { return playerRepository.getPlayerByProfileId(profileId); }

    public void insertProgression(ProgressionEntity progressionEntity) { progressionRepository.insert(progressionEntity); }

    public ProgressionEntity getLastProgressionEntityByProfileId(String profileId) { return progressionRepository.getLastProgressionEntityByProfileId(profileId); }

    public void insertSeason(SeasonEntity seasonEntity) { seasonRepository.insert(seasonEntity); }

    public SeasonEntity getLastSeasonEntityByProfileIdAndRegion(String profileId, String regionId, String skip, String count) { return seasonRepository.getLastSeasonEntityByProfileIdAndRegionId(profileId, regionId, skip, count); }

    public List<SeasonEntity> getSeasonEntityHistoryByProfileId(String profileId, String skip, String count) { return seasonRepository.getSeasonEntityHistoryByProfileId(profileId, skip, count); }

    public void insertStats(StatsEntity statsEntity) { statsRepository.insert(statsEntity); }

    public StatsEntity getLastStatsByProfileId(String profileId) { return statsRepository.getLastStatsEntityByProfileId(profileId); }

    public StatsEntity getStatsEntityByProfileIdAndGreaterThanDate(String profileId, Date updateDate) { return statsRepository.getStatsEntityByProfileIdAndGreaterThanDate(profileId, updateDate); }

    public StatsEntity getStatsEntityByProfileIdAndLessThanDate(String profileId, Date updateDate) { return statsRepository.getStatsEntityByProfileIdAndLessThanDate(profileId, updateDate); }

    public void insertSync(SyncEntity syncEntity) { syncRepository.insert(syncEntity); }

    public void updateSync(SyncEntity syncEntity) { syncRepository.update(syncEntity); }

    public SyncEntity getSyncByProfileId(String profileId) { return syncRepository.getSyncByProfileId(profileId); }

    public void delete(PlayerEntity playerEntity) { playerRepository.delete(playerEntity); }

}
