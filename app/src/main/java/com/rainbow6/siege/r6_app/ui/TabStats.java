package com.rainbow6.siege.r6_app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;

import static com.rainbow6.siege.r6_app.ui.PlayerActivity.PLAYER;

public class TabStats extends Fragment {

    private PlayerEntity playerEntity;
    private TextView playerNameItemView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        return rootView;
    }

    public void setPlayerEntity(PlayerEntity playerEntity){
        this.playerEntity = playerEntity;
    }
}
