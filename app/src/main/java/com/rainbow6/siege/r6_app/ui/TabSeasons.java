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

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.service.UbiService.REGION_NCSA;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.FORMAT_PRECISION_KD;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.FORMAT_PRECISION_WL;
import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.getDrawable;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.COUNT_1;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.SKIP_0;

public class TabSeasons extends Fragment {

    private PlayerEntity playerEntity;
    private View rootView;
    private PlayerViewModel playerViewModel;
    private static TabSeasons tabStatsRunningInstance;
    private Activity activity;

    public static final String DATE_FORMAT = "dd/MM/yy HH:mm";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_seasons, container, false);

        activity = getActivity();

        tabStatsRunningInstance = this;

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        SeasonEntity seasonEmeaEntity = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(playerEntity.getProfileId(), REGION_EMEA, SKIP_0, COUNT_1);
        SeasonEntity seasonNcsaEntity = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(playerEntity.getProfileId(), REGION_NCSA, SKIP_0, COUNT_1);

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

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
                            ftr.detach(TabSeasons.this).attach(TabSeasons.this).commit();
                        }
                    }catch(Exception e){
                        Log.d("Debug---Exception", e.getMessage());
                    }
                }
            });
        }
    }

    public static TabSeasons getInstance() { return tabStatsRunningInstance; }

}
