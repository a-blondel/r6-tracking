package com.rainbow6.siege.r6_app.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_NCSA;
import static com.rainbow6.siege.r6_app.ui.PlayerActivity.PLAYER;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.FORMAT_PRECISION_KD;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.FORMAT_PRECISION_WL;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.getDrawable;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.COUNT_1;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.SKIP_0;

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
        SeasonEntity seasonEmeaEntity = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(playerEntity.getProfileId(), REGION_EMEA, SKIP_0, COUNT_1);
        SeasonEntity seasonNcsaEntity = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(playerEntity.getProfileId(), REGION_NCSA, SKIP_0, COUNT_1);
        StatsEntity statsEntity = playerViewModel.getLastStatsByProfileId(playerEntity.getProfileId());

        // Level
        TextView textViewLevel = rootView.findViewById(R.id.level);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        textViewLevel.setText(getString(R.string.level, progressionEntity.getLevel(), sdf.format(progressionEntity.getUpdateDate())));

        // EMEA season
        TextView textViewEmeaTitle = rootView.findViewById(R.id.seasonEmea);
        String updateEmea = " (Not updated)";
        if(seasonEmeaEntity.getUpdateDate() != null){
            updateEmea = " (updated " + sdf.format(seasonEmeaEntity.getUpdateDate()) +")";
        }
        textViewEmeaTitle.setText(getString(R.string.season_details, REGION_EMEA.toUpperCase(), seasonEmeaEntity.getSeason(), updateEmea));
        ImageView imageViewEmeaRank = rootView.findViewById(R.id.seasonEmeaRank);
        imageViewEmeaRank.setImageResource(getDrawable(getActivity(), "rank_" + seasonEmeaEntity.getRank()));
        TextView textViewMmrEmea = rootView.findViewById(R.id.seasonEmeaRankPreviousNext);
        textViewMmrEmea.setText(Html.fromHtml(getString(R.string.season_mmr, seasonEmeaEntity.getPreviousRankMmr().intValue(), (int) Math.floor(seasonEmeaEntity.getMmr()), seasonEmeaEntity.getNextRankMmr().intValue(), (int) Math.floor(seasonEmeaEntity.getMaxMmr()))));
        ImageView imageViewEmeaMaxRank = rootView.findViewById(R.id.seasonEmeaMaxRank);
        imageViewEmeaMaxRank.setImageResource(getDrawable(getActivity(), "rank_" + seasonEmeaEntity.getMaxRank()));
        TextView textViewEmeaWins = rootView.findViewById(R.id.seasonEmeaWins);
        textViewEmeaWins.setText(getString(R.string.wins, seasonEmeaEntity.getWins()));
        TextView textViewEmeaLosses = rootView.findViewById(R.id.seasonEmeaLosses);
        textViewEmeaLosses.setText(getString(R.string.losses, seasonEmeaEntity.getLosses()));
        TextView textViewEmeaWlRatio = rootView.findViewById(R.id.seasonEmeaWlRatio);
        textViewEmeaWlRatio.setText(getString(R.string.wlRatio, String.format(FORMAT_PRECISION_WL, seasonEmeaEntity.getWins() / (double) seasonEmeaEntity.getLosses())));
        TextView textViewEmeaAbandons = rootView.findViewById(R.id.seasonEmeaAbandons);
        textViewEmeaAbandons.setText(getString(R.string.abandons, seasonEmeaEntity.getAbandons()));

        // Show ncsa stats when existing
        if(seasonNcsaEntity != null && Double.compare(seasonNcsaEntity.getMmr(), 2500) != 0){
            TextView textViewNcsaTitle = rootView.findViewById(R.id.seasonNcsa);
            String updateNcsa = " (Not updated)";
            if(seasonNcsaEntity.getUpdateDate() != null){
                updateNcsa = " (updated " + sdf.format(seasonNcsaEntity.getUpdateDate()) +")";
            }
            textViewNcsaTitle.setText(getString(R.string.season_details, REGION_NCSA.toUpperCase(), seasonNcsaEntity.getSeason(), updateNcsa));
            ImageView imageViewNcsaRank = rootView.findViewById(R.id.seasonNcsaRank);
            imageViewNcsaRank.setImageResource(getDrawable(getActivity(), "rank_" + seasonNcsaEntity.getRank()));
            TextView textViewMmrNcsa = rootView.findViewById(R.id.seasonNcsaRankPreviousNext);
            textViewMmrNcsa.setText(Html.fromHtml(getString(R.string.season_mmr, seasonNcsaEntity.getPreviousRankMmr().intValue(), (int) Math.floor(seasonNcsaEntity.getMmr()), seasonNcsaEntity.getNextRankMmr().intValue(), (int) Math.floor(seasonNcsaEntity.getMaxMmr()))));
            ImageView imageViewNcsaMaxRank = rootView.findViewById(R.id.seasonNcsaMaxRank);
            imageViewNcsaMaxRank.setImageResource(getDrawable(getActivity(), "rank_" + seasonNcsaEntity.getMaxRank()));
            TextView textViewNcsaWins = rootView.findViewById(R.id.seasonNcsaWins);
            textViewNcsaWins.setText(getString(R.string.wins, seasonNcsaEntity.getWins()));
            TextView textViewNcsaLosses = rootView.findViewById(R.id.seasonNcsaLosses);
            textViewNcsaLosses.setText(getString(R.string.losses, seasonNcsaEntity.getLosses()));
            TextView textViewNcsaWlRatio = rootView.findViewById(R.id.seasonNcsaWlRatio);
            textViewNcsaWlRatio.setText(getString(R.string.wlRatio, String.format(FORMAT_PRECISION_WL, seasonNcsaEntity.getWins() / (double) seasonNcsaEntity.getLosses())));
            TextView textViewNcsaAbandons = rootView.findViewById(R.id.seasonNcsaAbandons);
            textViewNcsaAbandons.setText(getString(R.string.abandons, seasonNcsaEntity.getAbandons()));
            // Set VISIBLE
            LinearLayout linearLayoutNcsaMain = rootView.findViewById(R.id.seasonNcsaMain);
            linearLayoutNcsaMain.setVisibility(View.VISIBLE);
            LinearLayout linearLayoutNcsaSecond = rootView.findViewById(R.id.seasonNcsaSecond);
            linearLayoutNcsaSecond.setVisibility(View.VISIBLE);
            LinearLayout linearLayoutNcsaThird = rootView.findViewById(R.id.seasonNcsaThird);
            linearLayoutNcsaThird.setVisibility(View.VISIBLE);
        }

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
        textViewPrecision.setText(getString(R.string.precision, String.format(FORMAT_PRECISION_WL, statsEntity.getGeneralBulletHit() / (double) statsEntity.getGeneralBulletFired())));

        TextView textViewHeadshots = rootView.findViewById(R.id.headshots);
        textViewHeadshots.setText(getString(R.string.headshots, String.format(FORMAT_PRECISION_WL, statsEntity.getGeneralHeadshots() / (double) statsEntity.getGeneralKills())));

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
