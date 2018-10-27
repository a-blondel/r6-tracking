package com.rainbow6.siege.r6_app.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.FORMAT_PRECISION_KD;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.FORMAT_PRECISION_WL;

public class TabStats extends Fragment {

    private PlayerEntity playerEntity;
    private View rootView;
    private PlayerViewModel playerViewModel;
    private static TabStats tabStatsRunningInstance;
    private Activity activity;

    public static final String DATE_FORMAT = "dd/MM/yy HH:mm";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player, container, false);

        activity = getActivity();

        tabStatsRunningInstance = this;

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        ProgressionEntity progressionEntity = playerViewModel.getLastProgressionEntityByProfileId(playerEntity.getProfileId());
        StatsEntity statsEntity = playerViewModel.getLastStatsByProfileId(playerEntity.getProfileId());

        // Level
        TextView textViewLevel = rootView.findViewById(R.id.level);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        textViewLevel.setText(getString(R.string.level, progressionEntity.getLevel(), sdf.format(progressionEntity.getUpdateDate())));

        // Stats
        TextView textViewStats = rootView.findViewById(R.id.statistics);
        textViewStats.setText(getString(R.string.statistics, sdf.format(statsEntity.getUpdateDate())));

        TextView textViewRankedKills = rootView.findViewById(R.id.rankedKills);
        textViewRankedKills.setText(getString(R.string.kills, statsEntity.getKillsRanked()));

        TextView textViewRankedDeaths = rootView.findViewById(R.id.rankedDeaths);
        textViewRankedDeaths.setText(getString(R.string.deaths, statsEntity.getDeathRanked()));

        TextView textViewRankedKdRatio = rootView.findViewById(R.id.rankedKdRatio);
        textViewRankedKdRatio.setText(getString(R.string.kdRatio, String.format(FORMAT_PRECISION_KD, statsEntity.getKillsRanked() / (double) statsEntity.getDeathRanked())));
        

        TextView textViewCasualKills = rootView.findViewById(R.id.casualKills);
        textViewCasualKills.setText(getString(R.string.kills, statsEntity.getKillsCasual()));

        TextView textViewCasualDeaths = rootView.findViewById(R.id.casualDeaths);
        textViewCasualDeaths.setText(getString(R.string.deaths, statsEntity.getDeathCasual()));

        TextView textViewCasualKdRatio = rootView.findViewById(R.id.casualKdRatio);
        textViewCasualKdRatio.setText(getString(R.string.kdRatio, String.format(FORMAT_PRECISION_WL, statsEntity.getKillsCasual() / (double) statsEntity.getDeathCasual())));

        TextView textViewRankedWins = rootView.findViewById(R.id.rankedWins);
        textViewRankedWins.setText(getString(R.string.wins, statsEntity.getMatchWonRanked()));

        TextView textViewRankedLosses = rootView.findViewById(R.id.rankedLosses);
        textViewRankedLosses.setText(getString(R.string.losses, statsEntity.getMatchLostRanked()));

        TextView textViewRankedWlRatio = rootView.findViewById(R.id.rankedWlRatio);
        textViewRankedWlRatio.setText(getString(R.string.wlRatio, String.format(FORMAT_PRECISION_WL, statsEntity.getMatchWonRanked() / (double) statsEntity.getMatchLostRanked())));

        TextView textViewCasualWins = rootView.findViewById(R.id.casualWins);
        textViewCasualWins.setText(getString(R.string.wins, statsEntity.getMatchWonCasual()));

        TextView textViewCasualLosses = rootView.findViewById(R.id.casualLosses);
        textViewCasualLosses.setText(getString(R.string.losses, statsEntity.getMatchLostCasual()));

        TextView textViewCasualWlRatio = rootView.findViewById(R.id.casualWlRatio);
        textViewCasualWlRatio.setText(getString(R.string.wlRatio, String.format(FORMAT_PRECISION_WL, statsEntity.getMatchWonCasual() / (double) statsEntity.getMatchLostCasual())));

        TextView textViewRankedKillsPerMatch = rootView.findViewById(R.id.rankedKillsPerGame);
        textViewRankedKillsPerMatch.setText(getString(R.string.kills_per_game, String.format(FORMAT_PRECISION_WL, statsEntity.getKillsRanked() / (double) statsEntity.getMatchPlayedRanked())));

        TextView textViewRankedDeathsPerMatch = rootView.findViewById(R.id.rankedDeathsPerGame);
        textViewRankedDeathsPerMatch.setText(getString(R.string.deaths_per_game, String.format(FORMAT_PRECISION_WL, statsEntity.getDeathRanked() / (double) statsEntity.getMatchPlayedRanked())));

        TextView textViewRankedKillsPerMin = rootView.findViewById(R.id.rankedKillsPerMin);
        textViewRankedKillsPerMin.setText(getString(R.string.kills_per_min, String.format(FORMAT_PRECISION_WL, statsEntity.getKillsRanked() / ((double) statsEntity.getTimePlayedRanked() / (double) 60))));

        TextView textViewCasualKillsPerMatch = rootView.findViewById(R.id.casualKillsPerGame);
        textViewCasualKillsPerMatch.setText(getString(R.string.kills_per_game, String.format(FORMAT_PRECISION_WL, statsEntity.getKillsCasual() / (double) statsEntity.getMatchPlayedCasual())));

        TextView textViewCasualDeathsPerMatch = rootView.findViewById(R.id.casualDeathsPerGame);
        textViewCasualDeathsPerMatch.setText(getString(R.string.deaths_per_game, String.format(FORMAT_PRECISION_WL, statsEntity.getDeathCasual() / (double) statsEntity.getMatchPlayedCasual())));

        TextView textViewCasualKillsPerMin = rootView.findViewById(R.id.casualKillsPerMin);
        textViewCasualKillsPerMin.setText(getString(R.string.kills_per_min, String.format(FORMAT_PRECISION_WL, statsEntity.getKillsCasual() / ((double) statsEntity.getTimePlayedCasual() / (double) 60))));

        TextView textViewRankedTimePlayed = rootView.findViewById(R.id.rankedTimePlayed);
        long seconds = statsEntity.getTimePlayedRanked();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        textViewRankedTimePlayed.setText(getString(R.string.time_played, String.format("%02dd%02dh%02dm", days, hours % 24, minutes % 60)));

        TextView textViewCasualTimePlayed = rootView.findViewById(R.id.casualTimePlayed);
        seconds = statsEntity.getTimePlayedCasual();
        minutes = seconds / 60;
        hours = minutes / 60;
        days = hours / 24;
        textViewCasualTimePlayed.setText(getString(R.string.time_played, String.format("%02dd%02dh%02dm", days, hours % 24, minutes % 60)));

        TextView textViewPrecision = rootView.findViewById(R.id.precision);
        textViewPrecision.setText(getString(R.string.precision, String.format(FORMAT_PRECISION_WL, statsEntity.getGeneralBulletHit() / (double) statsEntity.getGeneralBulletFired() * 100)));

        TextView textViewHeadshots = rootView.findViewById(R.id.headshots);
        textViewHeadshots.setText(getString(R.string.headshots, String.format(FORMAT_PRECISION_WL, statsEntity.getGeneralHeadshots() / (double) statsEntity.getGeneralKills() * 100)));

        TextView textViewAssists = rootView.findViewById(R.id.assists);
        textViewAssists.setText(getString(R.string.assists, statsEntity.getGeneralKillAssists()));

        TextView textViewRevives = rootView.findViewById(R.id.revives);
        textViewRevives.setText(getString(R.string.revives, statsEntity.getGeneralRevive()));

        TextView textViewMelee = rootView.findViewById(R.id.melee);
        textViewMelee.setText(getString(R.string.melee, statsEntity.getGeneralMeleeKills()));

        TextView textViewPenetration = rootView.findViewById(R.id.penetration);
        textViewPenetration.setText(getString(R.string.penetration, statsEntity.getGeneralPenetrationKills()));

        TextView textViewDbno = rootView.findViewById(R.id.dbno);
        textViewDbno.setText(getString(R.string.dbno, statsEntity.getGeneralPvpDbno()));

        TextView textViewDbnoAssists = rootView.findViewById(R.id.dbnoAssists);
        textViewDbnoAssists.setText(getString(R.string.dbnoAssists, statsEntity.getGeneralPvpDbnoAssists()));

        TextView textViewSuicide = rootView.findViewById(R.id.suicide);
        textViewSuicide.setText(getString(R.string.suicide, statsEntity.getGeneralPvpSuicide()));

        TextView textViewGadgetDestroyed = rootView.findViewById(R.id.gadgetDestroyed);
        textViewGadgetDestroyed.setText(getString(R.string.gadgetDestroyed, statsEntity.getGeneralPvpGadgetDestroy()));

        TextView textViewBlind = rootView.findViewById(R.id.blind);
        textViewBlind.setText(getString(R.string.blind, statsEntity.getGeneralPvpBlindKills()));

        TextView textViewRappelBreach = rootView.findViewById(R.id.rappelBreach);
        textViewRappelBreach.setText(getString(R.string.rappelBreach, statsEntity.getGeneralPvpRappelBreach()));

        TextView textViewBarricadeDeployed = rootView.findViewById(R.id.barricadeDeployed);
        textViewBarricadeDeployed.setText(getString(R.string.barricadeDeployed, statsEntity.getGeneralPvpBarricadeDeployed()));

        TextView textViewReinforcementDeploy = rootView.findViewById(R.id.reinforcementDeployed);
        textViewReinforcementDeploy.setText(getString(R.string.reinforcementDeployed, statsEntity.getGeneralPvpReinforcementDeploy()));

        /*TextView textViewDistanceTravelled = rootView.findViewById(R.id.distanceTravelled);
        textViewDistanceTravelled.setText(getString(R.string.distanceTravelled, statsEntity.getGeneralPvpDistanceTravelled()));*/

        TextView textViewReviveDenied = rootView.findViewById(R.id.reviveDenied);
        textViewReviveDenied.setText(getString(R.string.reviveDenied, statsEntity.getGeneralPvpReviveDenied()));

        // Get Win percentage : total wins / total games * 100

        return rootView;
    }

    public void updateUI() {
        if(activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        if (getFragmentManager() != null) {
                            FragmentTransaction ftr = getFragmentManager().beginTransaction();
                            ftr.detach(TabStats.this).attach(TabStats.this).commit();
                        }
                    }catch(Exception e){
                        Log.d("Debug---Exception", e.getMessage());
                    }
                }
            });
        }
    }

    public static TabStats getInstance() { return tabStatsRunningInstance; }

}
