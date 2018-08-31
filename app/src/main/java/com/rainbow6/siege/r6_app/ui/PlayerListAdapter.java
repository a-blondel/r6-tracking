package com.rainbow6.siege.r6_app.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.R;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerNameItemView;
        private final ImageView playerRankImageItemView;

        private PlayerViewHolder(View itemView) {
            super(itemView);
            playerNameItemView = itemView.findViewById(R.id.playerNameText);
            playerRankImageItemView = itemView.findViewById(R.id.playerRankImage);
        }
    }

    private final LayoutInflater mInflater;
    private List<PlayerEntity> mPlayerEntities; // Cached copy of players

    public PlayerListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        if (mPlayerEntities != null) {
            PlayerEntity current = mPlayerEntities.get(position);
            holder.playerNameItemView.setText(current.getNameOnPlatform());
//            holder.playerRankImageItemView.set
        } else {
            // Covers the case of data not being ready yet.
            holder.playerNameItemView.setText("No PlayerEntity");
        }
    }

    public void setPlayers(List<PlayerEntity> playerEntities) {
        mPlayerEntities = playerEntities;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPlayerEntities has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlayerEntities != null)
            return mPlayerEntities.size();
        else return 0;
    }
}