package com.rainbow6.siege.r6_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import static com.rainbow6.siege.r6_app.ui.PlayerActivity.PLAYER;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.COUNT_1;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.SKIP_0;

public class TabStats extends Fragment {

    private PlayerEntity playerEntity;
    private View rootView;
    private PlayerViewModel playerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player, container, false);

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);

        PlayerActivity activity = (PlayerActivity) getActivity();
        playerEntity = activity.getPlayerEntity();

        ProgressionEntity progressionEntity = playerViewModel.getLastProgressionEntityByProfileId(playerEntity.getProfileId());
        SeasonEntity seasonEmeaEntity = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(playerEntity.getProfileId(), REGION_EMEA, SKIP_0, COUNT_1);
        SeasonEntity seasonNcsaEntity = playerViewModel.getLastSeasonEntityByProfileIdAndRegion(playerEntity.getProfileId(), REGION_NCSA, SKIP_0, COUNT_1);
        StatsEntity statsEntity = playerViewModel.getLastStatsByProfileId(playerEntity.getProfileId());

        // Level
        TextView textViewLevel = rootView.findViewById(R.id.level);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        textViewLevel.setText(getString(R.string.level, progressionEntity.getLevel(), sdf.format(progressionEntity.getUpdateDate())));

        // Show ncsa stats when existing
        if(seasonNcsaEntity != null && Double.compare(seasonNcsaEntity.getMmr(), 2500) != 0){
            // set GONE to VISIBLE
        }

        return rootView;
    }

}
